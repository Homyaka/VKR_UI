/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constraintchoco;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.real.RealConstraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;


/**
 *
 * @author User
 */
class planstep{
    public planstep prev;
    public int stime;
    public String tr;
    public String ld;
    public String dz;
    public int sndr;
    public int sndt;

    planstep(int t, String str, String sld, String sdz, int sr, int st){
        prev=null;
        stime=t;
        tr=str;
        ld=sld;
        dz=sdz;
        sndr=sr;
        sndt=st;
    }
    
    @Override
    public String toString(){
        return "time:"+stime+" truck:"+tr+" loader:"+ld+" bulldozer:"+dz+" sand on the route:"+sndr+" total sand:"+sndt+"\n";
    }
    
    public String buildplan(){
        String ret="";
        planstep temp=this;
        while(temp!=null){
            ret=temp.toString()+ret;
            temp=temp.prev;
        }
        return ret;
    }
}

public class Spill {
        
        Model model = new Model("spill problem");
        IntVar tr1 = model.intVar("truck1", 0,7,false);
        IntVar trt1 = model.intVar("trucktime1", 0,360,true);
        IntVar trt2 = model.intVar("trucktime2", 0,360,true);
        IntVar tr2 = model.intVar("truck2", 0,7,false); 
        IntVar ld1 = model.intVar("loader1", 0,5,false); 
        IntVar ldt1 = model.intVar("loadertime1", 0,360,true);
        IntVar ldt2 = model.intVar("loadertime2", 0,360,true);
        IntVar ld2 = model.intVar("loader2", 0,5,false);
        IntVar dz1 = model.intVar("bulldozer1", 0,5,false);
        IntVar dzt1 = model.intVar("bulldozertime1", 0,360,true);
        IntVar dzt2 = model.intVar("bulldozertime2", 0,360,true);
        IntVar dz2 = model.intVar("bulldozer2", 0,5,false);
        IntVar sndr = model.intVar("sandinroad", 0, 2000, true);
        IntVar sndr2 = model.intVar("sandinroad2", 0, 2000, true);
        IntVar sndt = model.intVar("sandtotaldelivered", 0, 2000, true);
        IntVar sndt2 = model.intVar("sandtotaldelivered2", 0, 2000, true);
        IntVar time = model.intVar("time", 0,360,true);

        Constraint state;
        String []trstr={"старт","в карьер","загрузка","к разливу","разгрузка","ожидание","домой","закончил"};
        String []ldstr={"старт","в карьер","погрузка","ожидание","домой","закончил"};
        String []dzstr={"старт","к разливу","работа","ожидание","домой","закончил"};
        List <planstep> laststeps=new ArrayList<>();
        
        
        public void realtest(double Vs){
            Model model1 = new Model("spill problem");
            RealVar y = model1.realVar("y", 0,4000,0.1);
            RealVar x = model1.realVar("x", 0,4000,0.1);
            //model1.post(new Constraint("eqd",new Realeqprop(x,Vs)),new Constraint("eqR",new RealVgVpprop(x,y,2)));

            
            /*RealVar Vsand = model.realVar("Sand",Vs);
            RealVar Vtot = model.realVar("total", 0, 500000, 0.1);
            RealConstraint rc = model.realIbexGenericConstraint("{1}=sqrt({0})",Vsand,Vtot);
            rc.post();
            Solver solver = model1.getSolver();
            List<Solution>sols = solver.findAllSolutions();
            for(int i=0; i<sols.size();i++)
            System.out.println(sols.get(0).toString());*/
            
            System.out.println(System.getProperty("java.library.path"));
        
        }
        
        public void prepare(){
           /*1*/model.ifThen(model.and(model.arithm(tr1, "=", 0),model.arithm(time, "-", trt1,"<",30)), model.and(model.arithm(tr2, "=", 0),model.arithm(trt2, "=", trt1)));
           /*4*/model.ifThen(model.and(model.arithm(tr1, "=", 0),model.arithm(time, "-", trt1,"=",30)), model.and(model.arithm(tr2, "=", 1),model.arithm(trt2, "=", time)));

           /*2*/model.ifThen(model.and(model.arithm(ld1, "=", 0),model.arithm(time, "-", ldt1,"<",30)), model.and(model.arithm(ld2, "=", 0),model.arithm(ldt2, "=", ldt1)));
           /*5*/model.ifThen(model.and(model.arithm(ld1, "=", 0),model.arithm(time, "-", ldt1,"=",30)), model.and(model.arithm(ld2, "=", 1),model.arithm(ldt2, "=", time)));
            
           /*3*/model.ifThen(model.and(model.arithm(dz1, "=", 0),model.arithm(time, "-", dzt1,"<",30)), model.and(model.arithm(dz2, "=", 0),model.arithm(dzt2, "=", dzt1)));
           /*6*/model.ifThen(model.and(model.arithm(dz1, "=", 0),model.arithm(time, "-", dzt1,"=",30)), model.and(model.arithm(dz2, "=", 1),model.arithm(dzt2, "=", time)));
            
           /*7*/model.ifThen(model.and(model.or(model.and(model.arithm(tr1, "=", 1),model.arithm(time, "-", trt1,"=",5)),model.arithm(tr1, "=", 5)),model.or(model.and(model.arithm(ld1, "=", 1),model.arithm(time, "-", ldt1,"=",5)),model.arithm(ld1, "=", 3))),model.and(model.arithm(tr2, "=", 2),model.arithm(trt2, "=", time),model.arithm(ld2, "=", 2),model.arithm(ldt2, "=", time),model.arithm(sndr2, "=", 210)));
           /*7**/model.ifThen(model.and(model.or(model.and(model.arithm(tr1, "=", 1),model.arithm(time, "-", trt1,"=",5)),model.arithm(tr1, "=", 5)),model.not(model.or(model.and(model.arithm(ld1, "=", 1),model.arithm(time, "-", ldt1,"=",5)),model.arithm(ld1, "=", 3)))),model.and(model.arithm(tr2, "=", 5),model.arithm(trt2, "=", time),model.arithm(sndr2, "=", sndr)));
           
           /*8*/model.ifThen(model.and(model.arithm(tr1, "=", 2),model.arithm(time, "-", trt1,"<",15)),model.and(model.arithm(tr2, "=", 2),model.arithm(trt2, "=", trt1)));
           /*9*/model.ifThen(model.and(model.arithm(ld1, "=", 2),model.arithm(time, "-", ldt1,"<",15)),model.and(model.arithm(ld2, "=", 2),model.arithm(ldt2, "=", ldt1)));
           /*10*/model.ifThen(model.and(model.arithm(tr1, "=", 2),model.arithm(time, "-", trt1,"=",15)),model.and(model.arithm(tr2, "=", 3),model.arithm(trt2, "=", time)));
           /*11*/model.ifThen(model.and(model.arithm(ld1, "=", 2),model.arithm(time, "-", ldt1,"=",15),model.arithm(sndr, "+", sndt, "<",1775)),model.and(model.arithm(ld2, "=", 3),model.arithm(ldt2, "=", time)));
           /*12*/model.ifThen(model.and(model.arithm(ld1, "=", 3),model.not(model.or(model.and(model.arithm(tr1, "=", 1),model.arithm(time, "-", trt1,"=",5)),model.arithm(tr1, "=", 5)))),model.and(model.arithm(ld2, "=", 3),model.arithm(ldt2, "=", time)));
           /*13*/model.ifThen(model.and(model.arithm(ld1, "=", 2),model.arithm(time, "-", ldt1,"=",15),model.arithm(sndr, "+", sndt, ">=",1775)),model.and(model.arithm(ld2, "=", 4),model.arithm(ldt2, "=", time)));
           /*14*/model.ifThen(model.and(model.arithm(tr1, "=", 3),model.arithm(time, "-", trt1,"=",5)),model.and(model.arithm(tr2, "=", 4),model.arithm(trt2, "=", time),model.arithm(sndt2,"=",sndt,"+",sndr),model.arithm(sndr2,"=",0)));
           /*14**/model.ifThen(model.arithm(tr1, "!=", 3),model.arithm(sndt2,"=",sndt));
           /*14***/model.ifThen(model.and(model.arithm(tr1, "!=", 3),model.arithm(tr1, "!=", 1)),model.arithm(sndr2,"=",sndr));
           /*15*/model.ifThen(model.and(model.arithm(tr1, "=", 4),model.arithm(time, "-", trt1,"=",5),model.arithm(sndt,"<",1775)),model.and(model.arithm(tr2, "=", 1),model.arithm(trt2, "=", time)));
           /*16*/model.ifThen(model.and(model.arithm(tr1, "=", 4),model.arithm(time, "-", trt1,"=",5),model.arithm(sndt,">=",1775)),model.and(model.arithm(tr2, "=", 6),model.arithm(trt2, "=", time)));

           /*17*/model.ifThen(model.and(model.arithm(dz1, "=", 1),model.arithm(time, "-", dzt1,"<",10)),model.and(model.arithm(dz2, "=", 1),model.arithm(dzt2, "=", dzt1)));
           /*18*/model.ifThen(model.and(model.arithm(dz1, "=", 1),model.arithm(time, "-", dzt1,"=",10)),model.and(model.arithm(dz2, "=", 3),model.arithm(dzt2, "=", time)));
           /*19*/model.ifThen(model.and(model.arithm(dz1, "=", 3),model.arithm(tr1, "=", 4),model.arithm(time, "-", trt1,"=",5)),model.and(model.arithm(dz2, "=", 2),model.arithm(dzt2, "=", time)));
           /*20*/model.ifThen(model.and(model.arithm(dz1, "=", 3),model.not(model.and(model.arithm(tr1, "=", 4),model.arithm(time, "-", trt1,"=",5)))),model.and(model.arithm(dz2, "=", 3),model.arithm(dzt2, "=", dzt1)));
           /*21*/model.ifThen(model.and(model.arithm(dz1, "=", 2),model.arithm(time, "-", dzt1,"<",15)),model.and(model.arithm(dz2, "=", 2),model.arithm(dzt2, "=", dzt1)));
           /*22*/model.ifThen(model.and(model.arithm(dz1, "=", 2),model.arithm(time, "-", dzt1,"=",15),model.arithm(sndt, "<", 1775)),model.and(model.arithm(dz2, "=", 3),model.arithm(dzt2, "=", time)));
           /*23*/model.ifThen(model.and(model.arithm(dz1, "=", 2),model.arithm(time, "-", dzt1,"=",15),model.arithm(sndt, ">=", 1775)),model.and(model.arithm(dz2, "=", 4),model.arithm(dzt2, "=", time)));

           /*24*/model.ifThen(model.and(model.arithm(tr1, "=", 6),model.arithm(time, "-", trt1,"=",5)),model.and(model.arithm(tr2, "=", 7),model.arithm(trt2, "=", time)));
           /*25*/model.ifThen(model.and(model.arithm(ld1, "=", 4),model.arithm(time, "-", ldt1,"=",5)),model.and(model.arithm(ld2, "=", 5),model.arithm(ldt2, "=", time)));
           /*26*/model.ifThen(model.and(model.arithm(dz1, "=", 4),model.arithm(time, "-", dzt1,"<",10)),model.and(model.arithm(dz2, "=", 4),model.arithm(dzt2, "=", dzt1)));
           /*27*/model.ifThen(model.and(model.arithm(dz1, "=", 4),model.arithm(time, "-", dzt1,"=",10)),model.and(model.arithm(dz2, "=", 5),model.arithm(dzt2, "=", time)));
           /*28*/model.ifThen(model.arithm(tr1, "=", 7),model.and(model.arithm(tr2, "=", 7),model.arithm(trt2, "=", trt1)));
           /*29*/model.ifThen(model.arithm(ld1, "=", 5),model.and(model.arithm(ld2, "=", 5),model.arithm(ldt2, "=", ldt1)));
           /*30*/model.ifThen(model.arithm(dz1, "=", 5),model.and(model.arithm(dz2, "=", 5),model.arithm(dzt2, "=", ldt1)));
        
           planstep start = new planstep(5,trstr[0],ldstr[0],dzstr[0],0,0);
           state = model.and(model.arithm(time, "=", 5),
                   model.arithm(tr1, "=", 0),model.arithm(trt1, "=", 0),
                   model.arithm(ld1, "=", 0),model.arithm(ldt1, "=", 0),
                   model.arithm(dz1, "=", 0),model.arithm(dzt1, "=", 0),
                   model.arithm(sndr, "=", 0),model.arithm(sndt, "=", 0));
           laststeps.add(start);
        }
        private void pst(){
            model.post(state);
        }
        private void unpst(){
            model.unpost(state);
        }
        
        private void buildstate(int stime, Solution sol){
            state = model.and(model.arithm(time, "=", stime),
                    model.arithm(tr1, "=", sol.getIntVal(tr2)),model.arithm(trt1, "=", sol.getIntVal(trt2)),
                    model.arithm(ld1, "=", sol.getIntVal(ld2)),model.arithm(ldt1, "=", sol.getIntVal(ldt2)),
                    model.arithm(dz1, "=", sol.getIntVal(dz2)),model.arithm(dzt1, "=", sol.getIntVal(dzt2)),
                    model.arithm(sndr, "=", sol.getIntVal(sndr2)),model.arithm(sndt, "=", sol.getIntVal(sndt2)));
        
        }
        
        public void solve(int last){
            model.getEnvironment().worldPush();
            pst();
            Solver solver = model.getSolver();
            planstep pv=laststeps.get(last);
            int tim = pv.stime+5;
            List<Solution> sols = solver.findAllSolutions();
            unpst();
            solver.reset();
            model.getEnvironment().worldPop();

            for(int i=0; i<sols.size(); i++){
                int lst;
                Solution sol = sols.get(i);
                int trs=sol.getIntVal(tr2);
                int lds=sol.getIntVal(ld2);
                int dzs=sol.getIntVal(dz2);
                planstep nw=new planstep(tim,trstr[trs],ldstr[lds],dzstr[dzs],sol.getIntVal(sndr2),sol.getIntVal(sndt2));
                /*System.out.println(nw.toString());
                System.out.println(sol.toString());*/
                
                nw.prev=pv;
                if(i>0){laststeps.add(nw); lst=laststeps.size()-1;}
                else {laststeps.set(last, nw); lst=last;}
                if(trs==7&&lds==5&&dzs==5){
                    System.out.println("Расписание:\n"+nw.buildplan());
                }
                else{
                    if(tim<360){
                        buildstate(tim,sol);
                        solve(lst);
                    }
                }
            }
        }
        
}
