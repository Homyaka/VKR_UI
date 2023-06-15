/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Autoshop {
    Model model;
    String filename;
    IntVar X11,X12,X21,X22,C11,C12,C21,C22,CX11,CX12,CX21,CX22,
            D1,D2,Q,Inc,Y1,Y2,X1,X2,PY1,PY2,P,Dif1,Dif2,H1,H2,H;
    Solution sol;
    public Autoshop(){
        model=new Model("Auto");
    }
private void build(boolean bounded,int type){
        if(bounded)filename="boundedsolutions.txt";
        else filename="unboundedsolutions.txt";
        X11= model.intVar("X11", 0, 111, bounded); 
        X12= model.intVar("X12", 0, 111, bounded); 
        X21= model.intVar("X21", 0, 66, bounded); 
        X22= model.intVar("X22", 0, 66, bounded);
        C11=model.intVar("C11",  new int[]{27, 30});
        C12=model.intVar("C12",  29);
        C21=model.intVar("C21",  new int[]{45, 50});
        C22=model.intVar("C22",  48);
        CX11=model.intVar("CX11", 0, 111*30, true);
        CX12=model.intVar("CX12", 0, 111*29, true);
        CX21=model.intVar("CX21", 0, 66*50, true);
        CX22=model.intVar("CX22", 0, 66*48, true);
        D1 =model.intVar("D1",  new int[]{0, 200}); 
        D2 =model.intVar("D2",  new int[]{0, 200}); 
        Constraint C1 = model.or(model.and(model.arithm(X11, "<=", 20),model.arithm(C11, "=", 30)),
                model.and(model.and(model.arithm(X11, ">", 20),model.arithm(C11, "=", 27))));
        C1.post();
        Constraint C3 = model.or(model.and(model.arithm(X21, "<=", 10),model.arithm(C21, "=", 50)),
                model.and(model.and(model.arithm(X21, ">", 10),model.arithm(C21, "=", 45))));
        C3.post();
        model.times(X11, C11, CX11).post();
        model.times(X12, C12, CX12).post();
        model.times(X21, C21, CX21).post();
        model.times(X22, C22, CX22).post();
        Constraint C5=model.or(model.and(model.or(model.arithm(CX11, "+", CX21, ">=", 2500),model.arithm(CX11, "+", CX21, "=", 0)),model.arithm(D1,"=",0)),
                model.and(model.arithm(CX11, "+", CX21, "<", 2500),model.arithm(CX11, "+", CX21, ">", 0),model.arithm(D1,"=",200)));
        C5.post();
        Constraint C6=model.or(model.and(model.or(model.arithm(CX12, "+", CX22, ">=", 2000),model.arithm(CX12, "+", CX22, "=", 0)),model.arithm(D2,"=",0)),
                model.and(model.arithm(CX12, "+", CX22, "<", 2000),model.arithm(CX12, "+", CX22, ">", 0),model.arithm(D2,"=",200)));
        C6.post();
        Q = model.intVar("Q", 0, 3000, true);
        //model.arithm(Q, ">=", 2995).post();
        Constraint CQ=model.arithm(Q, "<=", 3000);
        CQ.post();
        model.sum(new IntVar[]{CX11,CX12,CX21,CX22,D1,D2}, "=", Q).post();
        switch (type){
            case 0:
                Y1= model.intVar("Y1", 47);
                Y2= model.intVar("Y2", 15);
                break;
            case 1:
                Y1= model.intVar("Y1", 53);
                Y2= model.intVar("Y2", 45);
                break;
            case 2:
                Y1= model.intVar("Y1", 47, 53,bounded);
                Y2= model.intVar("Y2", 15, 45,bounded);
            }
        X1= model.intVar("X1", 0, 222, true); 
        X2= model.intVar("X2", 0, 132, true);
        PY1= model.intVar("PY1", 47*39, 53*39, true); 
        PY2= model.intVar("PY2", 15*65, 45*65, true);
        P= model.intVar("P", 0, 45*65+53*39, true);
        IntVar PQ= model.intVar("P", 0, 45*65+53*39, true);
        model.arithm(PY1, "+", PY2, "=", P).post();
        model.times(Y1, 39, PY1).post();
        model.times(Y2, 65, PY2).post();
        model.arithm(X11,"+",X12,"=",X1).post();
        model.arithm(X21,"+",X22,"=",X2).post();
        Dif1 = model.intVar("Dif1", 0, 222, true);
        Dif2 = model.intVar("Dif2", 0, 132, true);
        model.arithm(X1,"-",Y1,"=",Dif1).post();
        model.arithm(X2,"-",Y2,"=",Dif2).post();
        H1= model.intVar("H1", 0, 222*5, true); 
        H2= model.intVar("H2", 0, 132*6, true);
        H= model.intVar("H", 0, 132*6+222*5, true);
        IntVar H11=model.intVar(5);
        IntVar H21=model.intVar(new int[]{2,6});
        Inc = model.intVar("INCOME", 0, 45*65+53*39, true);
        Constraint C8 = model.times(Dif1, H11, H1);
        C8.post();
        Constraint C9 = model.and(model.or(model.and(model.arithm(Dif2, "<=", 30),model.arithm(H21, "=", 2)),
                model.and(model.arithm(Dif2, ">", 30),model.arithm(H21, "=", 6))),
                model.times(Dif2, H21, H2));
        C9.post();
        model.arithm(H1, "+", H2, "=", H).post();
        model.arithm(P, "-", Q, "=", PQ).post();
        model.arithm(PQ, "-", H, "=", Inc).post();
        Constraint C10 = model.arithm(Inc,">=",600);
        C10.post();
        sol=new Solution(model);
        model.setObjective(true, Inc);

    }

private String soltostr(Solution sl){
    String ret="";
    ret+="Продавец 1:\n";
    ret+="Товар 1:\n";
    ret+="Количество: "+sl.getIntVal(X11)+" Цена: "+sl.getIntVal(C11)+" Сумма: "+sl.getIntVal(CX11)+"\n";
    ret+="Товар 2:\n";
    ret+="Количество: "+sl.getIntVal(X21)+" Цена: "+sl.getIntVal(C21)+" Сумма: "+sl.getIntVal(CX21)+"\n";
    ret+="Доставка: "+sl.getIntVal(D1)+"\n";
    ret+="Продавец 2:\n";
    ret+="Товар 1:\n";
    ret+="Количество: "+sl.getIntVal(X12)+" Цена: "+sl.getIntVal(C12)+" Сумма: "+sl.getIntVal(CX12)+"\n";
    ret+="Товар 2:\n";
    ret+="Количество: "+sl.getIntVal(X22)+" Цена: "+sl.getIntVal(C22)+" Сумма: "+sl.getIntVal(CX22)+"\n";
    ret+="Доставка: "+sl.getIntVal(D2)+"\n";
    ret+="Всего расходов на закупку: "+sl.getIntVal(Q)+"\n\n";
    ret+="Реализовано:\n";
    ret+="Товар 1:\n";
    ret+="Количество: "+sl.getIntVal(Y1)+" Цена: 39 Сумма: "+sl.getIntVal(PY1)+"\n";
    ret+="Осталось на складе: "+sl.getIntVal(Dif1)+" Стоимость хранения: "+sl.getIntVal(H1)+"\n";
    ret+="Товар 2:\n";
    ret+="Количество: "+sl.getIntVal(Y2)+" Цена: 65 Сумма: "+sl.getIntVal(PY2)+"\n";
    ret+="Осталось на складе: "+sl.getIntVal(Dif2)+" Стоимость хранения: "+sl.getIntVal(H2)+"\n\n";

    ret+="Всего доходов: "+sl.getIntVal(P)+" Всего расходов на хранение: "+sl.getIntVal(H)+"\n";
    ret+="Прибыль: "+sl.getIntVal(Inc)+"\n\n\n";

//X11,X12,X21,X22,C11,C12,C21,C22,CX11,CX12,CX21,CX22,D1,D2,Inc;
    return ret;
}

public void solve(boolean bound, int type) throws IOException{
    build(bound,type);
    File file = new File("E:\\"+filename);
    file.createNewFile();
    PrintWriter fout = new PrintWriter(file.getAbsoluteFile());
    Solver solver = model.getSolver();
    //List<Solution> sols = solver.findAllSolutions(); 
    //for(int i=0; i<sols.size(); i++)
    //    System.out.println(sols.get(i).toString());
    int count=0;
    String out;
    while(solver.solve()){
        count++;
        sol.record();
        out=soltostr(sol);
        System.out.println(out);
        fout.append(out);}
    
    out="Всего решений: "+count;
    
    System.out.println(out);
    fout.append(out);
    //System.out.println("SIZE: "+sols.size());
    fout.close();
    }
    
}
