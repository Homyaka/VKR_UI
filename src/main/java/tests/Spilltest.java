/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Spilltest {
    Model model;
    Spillstep steps[];
    String []trstr={"старт","в карьер","загрузка","к разливу","разгрузка","ожидание","домой","закончил"};
    String []ldstr={"старт","в карьер","погрузка","ожидание","домой","закончил"};
    String []dzstr={"старт","к разливу","работа","ожидание","домой","закончил"};

    public Spilltest (int i){
        model = new Model("spill problem");
        IntVar A = model.intVar("A", 0,2,false);
        IntVar B = model.intVar("B", 0,2,false);
        model.ifThen(model.arithm(A, "=", 1), model.arithm(B, "=", 2));
        model.ifThen(model.arithm(A, "=", 0), model.arithm(B, "=", 1));
        model.arithm(B, "=", 2).post();
        Solver solver = model.getSolver();
        while(solver.solve()){
            Solution sol = new Solution(model).record();
            String output="A ="+sol.getIntVal(A);
            output+=" B ="+sol.getIntVal(B);
            System.out.println(output);
            System.out.println("\n\n");
        }

        
    }
    
    public Spilltest(){
        model = new Model("spill problem");
        steps=new Spillstep[72];
        steps[0]=new Spillstep(model,5);
        
        for (int i=0; i<71; i++){
            steps[i+1]=new Spillstep(model,(i+2)*5);
           /*1*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 0),model.arithm(steps[i].trt,">",steps[i].time-30)), model.and(model.arithm(steps[i+1].tr, "=", 0),model.arithm(steps[i+1].trt, "=", steps[i].trt)));
           /*4*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 0),model.arithm(steps[i].trt,"=",steps[i].time-30)), model.and(model.arithm(steps[i+1].tr, "=", 1),model.arithm(steps[i+1].trt, "=", steps[i].time)));

           /*2*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 0),model.arithm(steps[i].ldt,">",steps[i].time-30)), model.and(model.arithm(steps[i+1].ld, "=", 0),model.arithm(steps[i+1].ldt, "=", steps[i].ldt)));
           /*5*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 0),model.arithm(steps[i].ldt,"=",steps[i].time-30)), model.and(model.arithm(steps[i+1].ld, "=", 1),model.arithm(steps[i+1].ldt, "=", steps[i].time)));
            
           /*3*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 0),model.arithm(steps[i].dzt,">",steps[i].time-30)), model.and(model.arithm(steps[i+1].dz, "=", 0),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
           /*6*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 0),model.arithm(steps[i].dzt,"=",steps[i].time-30)), model.and(model.arithm(steps[i+1].dz, "=", 1),model.arithm(steps[i+1].dzt, "=", steps[i].time)));
            
           /*7*/model.ifThen(model.and(model.or(model.and(model.arithm(steps[i].tr, "=", 1),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.arithm(steps[i].tr, "=", 5)),model.or(model.and(model.arithm(steps[i].ld, "=", 1),model.arithm(steps[i].ldt,"=",steps[i].time-5)),model.arithm(steps[i].ld, "=", 3))),model.and(model.arithm(steps[i+1].tr, "=", 2),model.arithm(steps[i+1].trt, "=", steps[i].time),model.arithm(steps[i+1].ld, "=", 2),model.arithm(steps[i+1].ldt, "=", steps[i].time),model.arithm(steps[i+1].sndr, "=", 210)));
           /*7**/model.ifThen(model.and(model.or(model.and(model.arithm(steps[i].tr, "=", 1),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.arithm(steps[i].tr, "=", 5)),model.not(model.or(model.and(model.arithm(steps[i].ld, "=", 1),model.arithm(steps[i].ldt,"=",steps[i].time-5)),model.arithm(steps[i].ld, "=", 3)))),model.and(model.arithm(steps[i+1].tr, "=", 5),model.arithm(steps[i+1].trt, "=", steps[i].time),model.arithm(steps[i+1].sndr, "=", steps[i].sndr)));
           
           /*8*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 2),model.arithm(steps[i].trt,">",steps[i].time-15)),model.and(model.arithm(steps[i+1].tr, "=", 2),model.arithm(steps[i+1].trt, "=", steps[i].trt)));
           /*9*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 2),model.arithm(steps[i].ldt,">",steps[i].time-15)),model.and(model.arithm(steps[i+1].ld, "=", 2),model.arithm(steps[i+1].ldt, "=", steps[i].ldt)));
           /*10*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 2),model.arithm(steps[i].trt,"=",steps[i].time-15)),model.and(model.arithm(steps[i+1].tr, "=", 3),model.arithm(steps[i+1].trt, "=", steps[i].time)));
           /*11*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 2),model.arithm(steps[i].ldt,"=",steps[i].time-15),model.arithm(steps[i].sndr, "+", steps[i].sndt, "<",1775)),model.and(model.arithm(steps[i+1].ld, "=", 3),model.arithm(steps[i+1].ldt, "=", steps[i].time)));
           /*12*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 3),model.not(model.or(model.and(model.arithm(steps[i].tr, "=", 1),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.arithm(steps[i].tr, "=", 5)))),model.and(model.arithm(steps[i+1].ld, "=", 3),model.arithm(steps[i+1].ldt, "=", steps[i].time)));
           /*13*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 2),model.arithm(steps[i].ldt,"=",steps[i].time-15),model.arithm(steps[i].sndr, "+", steps[i].sndt, ">=",1775)),model.and(model.arithm(steps[i+1].ld, "=", 4),model.arithm(steps[i+1].ldt, "=", steps[i].time)));
           /*14*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 3),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.and(model.arithm(steps[i+1].tr, "=", 4),model.arithm(steps[i+1].trt, "=", steps[i].time),model.arithm(steps[i+1].sndt,"=",steps[i].sndt,"+",steps[i].sndr),model.arithm(steps[i+1].sndr,"=",0)));
           /*14**/model.ifThen(model.arithm(steps[i].tr, "!=", 3),model.arithm(steps[i+1].sndt,"=",steps[i].sndt));
           /*14***/model.ifThen(model.and(model.arithm(steps[i].tr, "!=", 3),model.arithm(steps[i].tr, "!=", 1)),model.arithm(steps[i+1].sndr,"=",steps[i].sndr));
           /*15*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 4),model.arithm(steps[i].trt,"=",steps[i].time-5),model.arithm(steps[i].sndt,"<",1775)),model.and(model.arithm(steps[i+1].tr, "=", 1),model.arithm(steps[i+1].trt, "=", steps[i].time)));
           /*16*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 4),model.arithm(steps[i].trt,"=",steps[i].time-5),model.arithm(steps[i].sndt,">=",1775)),model.and(model.arithm(steps[i+1].tr, "=", 6),model.arithm(steps[i+1].trt, "=", steps[i].time)));

           /*17*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 1),model.arithm(steps[i].dzt,">",steps[i].time-10)),model.and(model.arithm(steps[i+1].dz, "=", 1),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
           /*18*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 1),model.arithm(steps[i].dzt,"=",steps[i].time-10)),model.and(model.arithm(steps[i+1].dz, "=", 3),model.arithm(steps[i+1].dzt, "=", steps[i].time)));
           /*19*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 3),model.arithm(steps[i].tr, "=", 4),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.and(model.arithm(steps[i+1].dz, "=", 2),model.arithm(steps[i+1].dzt, "=", steps[i].time)));
           /*20*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 3),model.not(model.and(model.arithm(steps[i].tr, "=", 4),model.arithm(steps[i].trt,"=",steps[i].time-5)))),model.and(model.arithm(steps[i+1].dz, "=", 3),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
           /*21*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 2),model.arithm(steps[i].dzt,">",steps[i].time-15)),model.and(model.arithm(steps[i+1].dz, "=", 2),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
           /*22*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 2),model.arithm(steps[i].dzt,"=",steps[i].time-15),model.arithm(steps[i].sndt, "<", 1775)),model.and(model.arithm(steps[i+1].dz, "=", 3),model.arithm(steps[i+1].dzt, "=", steps[i].time)));
           /*23*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 2),model.arithm(steps[i].dzt,"=",steps[i].time-15),model.arithm(steps[i].sndt, ">=", 1775)),model.and(model.arithm(steps[i+1].dz, "=", 4),model.arithm(steps[i+1].dzt, "=", steps[i].time)));

           /*24*/model.ifThen(model.and(model.arithm(steps[i].tr, "=", 6),model.arithm(steps[i].trt,"=",steps[i].time-5)),model.and(model.arithm(steps[i+1].tr, "=", 7),model.arithm(steps[i+1].trt, "=", steps[i].time)));
           /*25*/model.ifThen(model.and(model.arithm(steps[i].ld, "=", 4),model.arithm(steps[i].ldt,"=",steps[i].time-5)),model.and(model.arithm(steps[i+1].ld, "=", 5),model.arithm(steps[i+1].ldt, "=", steps[i].time)));
           /*26*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 4),model.arithm(steps[i].dzt,">",steps[i].time-10)),model.and(model.arithm(steps[i+1].dz, "=", 4),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
           /*27*/model.ifThen(model.and(model.arithm(steps[i].dz, "=", 4),model.arithm(steps[i].dzt,"=",steps[i].time-10)),model.and(model.arithm(steps[i+1].dz, "=", 5),model.arithm(steps[i+1].dzt, "=", steps[i].time)));
           /*28*/model.ifThen(model.arithm(steps[i].tr, "=", 7),model.and(model.arithm(steps[i+1].tr, "=", 7),model.arithm(steps[i+1].trt, "=", steps[i].trt)));
           /*29*/model.ifThen(model.arithm(steps[i].ld, "=", 5),model.and(model.arithm(steps[i+1].ld, "=", 5),model.arithm(steps[i+1].ldt, "=", steps[i].ldt)));
           /*30*/model.ifThen(model.arithm(steps[i].dz, "=", 5),model.and(model.arithm(steps[i+1].dz, "=", 5),model.arithm(steps[i+1].dzt, "=", steps[i].dzt)));
        }
        model.and(
                   model.arithm(steps[0].tr, "=", 0),model.arithm(steps[0].trt, "=", 0),
                   model.arithm(steps[0].ld, "=", 0),model.arithm(steps[0].ldt, "=", 0),
                   model.arithm(steps[0].dz, "=", 0),model.arithm(steps[0].dzt, "=", 0),
                   model.arithm(steps[0].sndr, "=", 0),model.arithm(steps[0].sndt, "=", 0)
        ).post();
        
    }
    public void solve(){
        Solver solver = model.getSolver();
        int count=1;
        while(solver.solve()){
            System.out.println("SOLUTION "+count);
            count ++;
            Solution sol = new Solution(model).record();
            for(int i=0; i<72; i++){
                String output="STEP "+(i+1)+" TIME: "+steps[i].time;
                output+=" TRUCK: "+trstr[sol.getIntVal(steps[i].tr)];
                output+=" LOADER: "+ldstr[sol.getIntVal(steps[i].ld)];
                output+=" DOZER: "+dzstr[sol.getIntVal(steps[i].dz)];
                output+=" SANDONROAD: "+sol.getIntVal(steps[i].sndr);
                output+=" TOTALSAND: "+sol.getIntVal(steps[i].sndt);
                System.out.println(output);
            }
            System.out.println("\n\n");
        }
    }
    
    
    
}
