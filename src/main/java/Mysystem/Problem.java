package Mysystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

import org.chocosolver.solver.variables.IntVar;

public class Problem {
    private final Model model;
    private final List<Variable> vars;
    private final Valuevocab domvals;
    private List<DSystem> problems;
    private int minSup;
    public HashMap<IntVar,Line> mapIntVarLine;
    public int getMinSup() {
        return minSup;
    }

    public void setMinSup(int minSup) {
        this.minSup = minSup;
    }

    private List<List<DSystem>> orderedsystems;
    private boolean prepared;
    private final Decisions decisions;
    private final List<Solution> solutions;

    public List<List<DSystem>> getOrderedsystems() {
        return orderedsystems;
    }
    public List<Variable> getVars() {
        return vars;
    }

    public List<DSystem> getProblems() {
        return problems;
    }

    public Decisions getDecisions() {
        return decisions;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    private Line getLine(){
        IntVar intVar=model.intVar(0,1);
        Line line=new Line();
        line.intVar=intVar;
        mapIntVarLine.put(intVar,line);
        return line;
    }
    private Variable getVariable(String name){
        for(int i=0; i<vars.size(); i++)
            if(vars.get(i).getName().compareTo(name)==0) return vars.get(i);
        return null;
    }
    
    /*public List<Mysystem> getactiveproblems(){
        List<Mysystem> ret = new ArrayList<>();
        for(int i=0; i<problems.size(); i++){
            Mysystem tmp= problems.get(i);
            if(tmp.isActive()&&(tmp.getActivecolumnscount()*tmp.getActivelinescount()!=0))ret.add(tmp);
        }
        return ret;
    }*/
    
    private List<Value> buildColumndomain(Variable tmp, String doms){
        List<Value> domarr = new ArrayList<>();
        Value tmpval = null;
        while (doms.contains(";")){
            int dind = domvals.getIndex(doms.substring(0, doms.indexOf(";")));
            if (dind ==-1){
                tmpval = new Value(domvals.addValue(doms.substring(0, doms.indexOf(";"))));
                if(tmp!=null)tmp.addValue(tmpval);
            }
            else{
                if(tmp!=null) tmpval = tmp.checkValue(domvals.getVocab().get(dind));
                else tmpval = new Value(domvals.getVocab().get(dind));
            }    
            domarr.add(tmpval);
            doms=doms.substring(doms.indexOf(";")+1);
        }
        int dind = domvals.getIndex(doms);
        if (dind ==-1){
            tmpval = new Value(domvals.addValue(doms));
            if(tmp!=null)tmp.addValue(tmpval);
        }
        else{
            if(tmp!=null) tmpval = tmp.checkValue(domvals.getVocab().get(dind));
            else tmpval = new Value(domvals.getVocab().get(dind));
        }
        domarr.add(tmpval);
        return domarr;
    }
    
    private void buildsys(DSystem prblm, List <List<String>> problem){
        int domnum=problem.size();
        for (int i=0; i<domnum;i++){
            String name = problem.get(i).get(0);
            String temp = problem.get(i).get(1);
            Variable tmp = getVariable(name);
            String doms = temp.substring(temp.indexOf("{")+1 , temp.indexOf("}"));
            List<Value> domarr = buildColumndomain(tmp,doms);
            if(tmp==null){
                tmp = new Variable(name,domarr);
                vars.add(tmp);
            }
            Column newvar = new Column(domarr);
            tmp.addColumn(newvar);
            prblm.addColumn(newvar);
            for (int j=2; j<problem.get(0).size();j++){
                if (prblm.getLines().size()<j-1) prblm.addLine(getLine());
                List<Value> nodevals = new ArrayList<>();
                String cons = problem.get(i).get(j);
                temp = cons.substring(cons.indexOf("{")+1 , cons.indexOf("}"));
                while (temp.contains(";")){
                    int indx =domvals.getCode(temp.substring(0, temp.indexOf(";"))); 
                    if (indx!=-1) nodevals.add(newvar.getValue(indx));
                    temp=temp.substring(temp.indexOf(";")+1);
                }
                int indx = domvals.getCode(temp);
                if (indx!=-1) nodevals.add(newvar.getValue(indx));
                Node newnode= new Node(nodevals);
                newvar.addNode(newnode);
                prblm.getLines().get(j-2).addNode(newnode);
            }
        }
    }
    
    public void buildqueen(int n){
       /* Nqueens nq = new Nqueens();
        nq.build(n, vars, domvals, problems, solutions, decisions);*/
    }
    public Problem(String name,int minSup){
        mapIntVarLine =new HashMap<>();
       this.minSup=minSup;
        model = new Model(name);
        model.getSolver().limitTime(120000);
        vars = new ArrayList<>();
        domvals = new Valuevocab();
        problems = new ArrayList<>();
        prepared = false;
        decisions = new Decisions();
        solutions = new ArrayList<>();
        orderedsystems = new ArrayList<>();
    }

    public void addsys(String name,List <List<String>> sys){
        DSystem prblm = new DSystem(name, solutions, decisions,this);
        buildsys(prblm,sys);
        problems.add(prblm);
    }

    public void addSys(String name,List <List<String>> sys,int tay){
        DSystem prblm = new DSystem(name,solutions,decisions,this);
    }

    public String prop(){
        preparechoco(false);
        Solver solver = model.getSolver();
        String ret="";
        ret+="\nPropogation\n";
        try {
            solver.propagate();
            for(int i=0; i<vars.size(); i++)
                ret+=vars.get(i).toString()+"\n";
        } catch (ContradictionException ex) {
            ret="System has no solutions\n";
        }
        return ret;
    }
    private void ordersystems(){
        for(int i=0; i<problems.size(); i++){
            DSystem sys = problems.get(i);
            int siz=sys.getColumns().size();
            if(orderedsystems.size()<siz)
                for (int j=orderedsystems.size(); j<siz; j++)
                    orderedsystems.add(new ArrayList<>());
            orderedsystems.get(siz-1).add(sys);
        }
    }

    private void preparechoco(boolean withtiming){
        long time= java.lang.System.currentTimeMillis();
        if(!prepared){
            //for(int i=0; i<vars.size(); i++)
               // vars.get(i).buildchoco(model);
            for(int i=0; i<problems.size(); i++){
                DSystem prblm = problems.get(i);
                Constraint c = new Constraint("D-system", new DPropagator(prblm));
                model.and(c).post();
                }
            }
            prepared=true;
            if(withtiming) java.lang.System.out.println("Preparation is done in "+(java.lang.System.currentTimeMillis()-time)+" ms");
        }
    


   /* public long solveone(boolean withtiming){
        preparechoco(withtiming);
        long time=System.currentTimeMillis();
        Solver solver = model.getSolver();
        String ret="";
        MyDvarselector varsel=new MyDvarselector(problems.get(0));
        MyDvalselector valsel=new MyDvalselector(decisions);
        MyDecoperator decop = new MyDecoperator(decisions);
        solver.setSearch((intVarSearch(varsel,valsel,decop,problems.get(0).getIntvars())));
        solver.findAllSolutions(null);
        if(withtiming)time=System.currentTimeMillis()-time;
        return time;
        
    }*/
    

    //new

    public List<Variable> getActiveVars() {
        List<Variable> ret = new ArrayList<>();
        for (int i=0; i<vars.size(); i++)
            if(vars.get(i).isActive())ret.add(vars.get(i));
        return ret;
    }
    
    
    
    public long solvemany(boolean withtiming){
        preparechoco(withtiming);
        long time= java.lang.System.currentTimeMillis();
        Solver solver = model.getSolver();
        DVariableSelector varsel=new DVariableSelector(this);
        DValueSelector valsel=new DValueSelector(decisions);
        Decoperator decop = new Decoperator(decisions);
        solver.setSearch((intVarSearch(varsel,valsel,decop,problems.get(0).getIntVars())));
        while(solver.solve())
            solutions.add(new Solution(vars,decisions.getDecisions()));
        if(withtiming)time= java.lang.System.currentTimeMillis()-time;
        return time;
    }
    
    public long firstsolvemany(boolean withtiming){
        preparechoco(withtiming);
        long time= java.lang.System.currentTimeMillis();
        Solver solver = model.getSolver();
        DVariableSelector varsel=new DVariableSelector(this);
        DValueSelector valsel=new DValueSelector(decisions);
        Decoperator decop = new Decoperator(decisions);
        solver.setSearch((intVarSearch(varsel,valsel,decop,problems.get(0).getIntVars())));
        solver.solve();
        solutions.add(new Solution(vars,decisions.getDecisions()));
        if(withtiming)time= java.lang.System.currentTimeMillis()-time;
        return time;
    }
    
        public String showsolutions(boolean justcount,boolean withdec){
            if (solutions.size()==0) return "Решений не найдено"+"\n"+"или содержимое файла" + "\n"+"некорректно!";
            String ret="Найдено "+ solutions.size()+" решений: \n";
            //String ret="Найдено "+ solutions.size()+" решений: \n";
        if(!justcount){
            for(int i=0; i<solutions.size();i++){
                ret+=solutions.get(i).solutiontoString(false);
                if(withdec)ret+=solutions.get(i).decisionstoString();
                ret+="\n";
            }
            if (ret.length()==0)ret="no solutions";
        }
       // ret+="\nTotal solutions:"+solutions.size();
        return ret;
    }
}
