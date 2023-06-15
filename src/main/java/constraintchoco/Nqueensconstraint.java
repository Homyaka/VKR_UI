/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constraintchoco;
import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;

/**
 *
 * @author User
 */
public class Nqueensconstraint {
    Model model;
    IntVar[] vars;

    private void test(){
        Model testmodel=new Model("Test");
        int x[]={1,2,3};
        int y[]={1,2,3};
        IntVar X = testmodel.intVar("X", x);
        IntVar Y = testmodel.intVar("Y", y);
        Constraint node11=testmodel.or(testmodel.arithm(X, "=", 2),testmodel.arithm(X, "=", 3));
        Constraint node12=testmodel.or(testmodel.arithm(Y, "=", 2),testmodel.arithm(Y, "=", 3));
        Constraint node21=testmodel.or(testmodel.arithm(X, "=", 1),testmodel.arithm(X, "=", 3));
        Constraint node22=testmodel.or(testmodel.arithm(Y, "=", 1),testmodel.arithm(Y, "=", 3));
        
        Constraint firstrow=testmodel.or(node11,node12);
        Constraint secondrow=testmodel.or(node11,node12);
        testmodel.and(firstrow,secondrow).post();
    }
    
    
    private void buildar(int n){
        model = new Model(n + "-queens problem");
        vars = new IntVar[n];
        for(int q = 0; q < n; q++){
            vars[q] = model.intVar("Q_"+q, 1, n);
        }
        for(int i  = 0; i < n-1; i++){
            for(int j = i + 1; j < n; j++){
                model.arithm(vars[i], "!=",vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
    }
    
    private void buildlog(int n){
        model = new Model(n + "-queens problem");
        vars = new IntVar[n];
        for(int q = 0; q < n; q++){
            vars[q] = model.intVar("Q_"+q, 0, n-1);
        }
        for(int i=0; i<n-1; i++)
            for(int j = i+1; j<n; j++)
                for(int ii=0; ii<n; ii++)
                    model.ifThen(model.arithm(vars[i], "=", ii), model.and(model.arithm(vars[j], "!=", ii),model.arithm(vars[j], "!=", ii+(j-i)),model.arithm(vars[j], "!=", ii-(j-i))));
    }

    private void buildlogst(int n){
        model = new Model(n + "-queens problem");
        vars = new IntVar[n];
        for(int q = 0; q < n; q++){
            vars[q] = model.intVar("Q_"+q, 0, n-1);
        }
        for(int i=0; i<n-1; i++)
            for(int j = i+1; j<n; j++){
                List<Constraint> ls = new ArrayList<>();
                for(int ii=0; ii<n; ii++){
                    List<Constraint> li = new ArrayList<>();
                    List<Constraint> lj = new ArrayList<>();
                    for(int jj=0; jj<n; jj++){
                        if(jj!=ii){
                            li.add(model.arithm(vars[i], "=", jj));
                            if((jj!=ii+(j-i))&&(jj!=ii-(j-i)))lj.add(model.arithm(vars[j], "=", jj));
                        }
                    }
                    ls.add(model.or(model.or(li.toArray(new Constraint[li.size()])),model.or(lj.toArray(new Constraint[lj.size()]))));
                }
                model.and(ls.toArray(new Constraint[ls.size()])).post();
            }
    }
    
public String solvefirst(int n){
    buildar(n);
    Solver solver=model.getSolver();
    solver.limitTime(180000);
    long time=System.currentTimeMillis();
    solver.solve();
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" time="+time);

    return ""+(Runtime.getRuntime().totalMemory())/1048576;
}

public String solveall(int n){
    buildar(n);
    Solver solver=model.getSolver();
    //solver.limitTime(120000);
    long time=System.currentTimeMillis();
    int count=0;
    while (solver.solve()) count++;
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" sols="+count+" time="+time);
    return ""+time+"    "+(Runtime.getRuntime().totalMemory())/1048576;
}
public long solvefirstlog(int n){
    buildlog(n);
    Solver solver=model.getSolver();
    //solver.limitTime(120000);
    long time=System.currentTimeMillis();
    solver.solve();
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" time="+time);

    return time;
}

public long solvefirstlogmem(int n){
    buildlog(n);
    Solver solver=model.getSolver();
    solver.limitTime(120000);
    solver.solve();

    return (Runtime.getRuntime().totalMemory())/1048576;
}


public long solvealllog(int n){
    buildlog(n);
    Solver solver=model.getSolver();
    solver.limitTime(120000);
    long time=System.currentTimeMillis();
    int count=0;
    while (solver.solve()) count++;
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" sols="+count+" time="+time);
    return time;
}
public long solvefirstlogst(int n){
    buildlogst(n);
    Solver solver=model.getSolver();
    solver.limitTime(120000);
    long time=System.currentTimeMillis();
    solver.solve();
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" time="+time);

    return time;
}

public long solvefirstlogmemst(int n){
    buildlogst(n);
    Solver solver=model.getSolver();
    solver.limitTime(120000);
    solver.solve();

    return (Runtime.getRuntime().totalMemory())/1048576;
}


public long solvealllogst(int n){
    buildlogst(n);
    Solver solver=model.getSolver();
    solver.limitTime(120000);
    long time=System.currentTimeMillis();
    int count=0;
    while (solver.solve()) count++;
    time=System.currentTimeMillis()-time;
    System.out.println("N="+n+" sols="+count+" time="+time);
    return time;
}
       
}
