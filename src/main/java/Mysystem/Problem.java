package Mysystem;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

import org.chocosolver.solver.variables.IntVar;

public class Problem {
    public HashMap<String,Solution> uniqueSolution;
    private final Model model;
    private final List<Variable> vars;
    private final HashSet<Integer> domvals;
    private List<DSystem> problems;
    private int minSup;
    public int containAtt;
    public int noContainAtt;
    public int patternLength;
    public ArrayList<Integer> subPattern;
    public ArrayList<Integer> superPattern;
    public HashMap<IntVar,Line> mapIntVarLine;
    public HashSet<String> setXsol;
    public HashMap<Line,Integer> addition;

    public int getMinSup() {
        return minSup;
    }

    public ArrayList<Integer> weights;

    public void setMinSup(int minSup) {
        this.minSup = minSup;
    }

    private List<List<DSystem>> orderedsystems;
    private boolean prepared;
    private final Decisions decisions;
    private final List<Solution> solutions;
    public ArrayList<Boolean[]> listBoolVectors;
    public long[] vectors;
    public int countAtt;

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

    private List<Value> buildColumndomain(Variable tmp, String doms){
        List<Value> domarr = new ArrayList<>();
        Value tmpval = null;
        while (doms.contains(";")){
            int dind =Integer.parseInt(doms.substring(0, doms.indexOf(";")));
            if (!domvals.contains(dind)){
                domvals.add(dind);
                tmpval = new Value(dind);
                if(tmp!=null)tmp.addValue(tmpval);
            }
            else{
                if(tmp!=null) tmpval = tmp.checkValue(dind);
                else tmpval = new Value(dind);
            }
            domarr.add(tmpval);
            doms=doms.substring(doms.indexOf(";")+1);
        }
        int dind = Integer.parseInt(doms);
        if (!domvals.contains(dind)){
            tmpval = new Value(dind);
            if(tmp!=null)tmp.addValue(tmpval);
        }
        else{
            if(tmp!=null) tmpval = tmp.checkValue(dind);
            else tmpval = new Value(dind);
        }
        domarr.add(tmpval);
        return domarr;
    }

    private void buildsys(DSystem prblm, List <List<String>> problem){
        int domnum=problem.size();
        String weight=problem.get(0).get(2);
        weight=weight.substring(weight.indexOf('{')+1,weight.indexOf('}'));
        String[] weightsArr=weight.split(";");
        weights=new ArrayList<>();
        for(int i=0;i<weightsArr.length;i++) weights.add(Integer.parseInt(weightsArr[i]));
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
            for (int j=3; j<problem.get(0).size();j++){
                if (prblm.getLines().size()<j-2) prblm.addLine(getLine());
                List<Value> nodevals = new ArrayList<>();
                String cons = problem.get(i).get(j);
                temp = cons.substring(cons.indexOf("{")+1 , cons.indexOf("}"));
                while (temp.contains(";")){
                    int indx =Integer.parseInt(temp.substring(0, temp.indexOf(";")));
                    nodevals.add(newvar.getValue(indx));
                    temp=temp.substring(temp.indexOf(";")+1);
                }
                int indx = Integer.parseInt(temp);
                nodevals.add(newvar.getValue(indx));
                Node newnode= new Node(nodevals);
                newvar.addNode(newnode);
                prblm.getLines().get(j-3).addNode(newnode);
            }
        }
    }

    public Problem(String name,int minSup){
        mapIntVarLine =new HashMap<>();
        this.minSup=minSup;
        containAtt=-1;
        noContainAtt=-1;
        patternLength=-1;
        subPattern=null;
        superPattern=null;
        model = new Model(name);
        model.getSolver().limitTime(120000);
        vars = new ArrayList<>();
        domvals = new HashSet<>();
        problems = new ArrayList<>();
        prepared = false;
        decisions = new Decisions();
        solutions = new ArrayList<>();
        orderedsystems = new ArrayList<>();
        listBoolVectors=new ArrayList<>();
        setXsol=new HashSet<>();
        addition=new HashMap<>();
    }

    public Problem(String name,int minSup,int containAtt,int noContainAtt,int patternLength, ArrayList<Integer> subPattern,ArrayList<Integer> superPattern){
        mapIntVarLine =new HashMap<>();
        this.minSup=minSup;
        this.containAtt=containAtt;
        this.noContainAtt=noContainAtt;
        this.patternLength=patternLength;
        this.superPattern=superPattern;
        this.subPattern=subPattern;
        model = new Model(name);
        model.getSolver().limitTime(120000);
        vars = new ArrayList<>();
        domvals = new HashSet<>();
        problems = new ArrayList<>();
        prepared = false;
        decisions = new Decisions();
        solutions = new ArrayList<>();
        orderedsystems = new ArrayList<>();
        listBoolVectors=new ArrayList<>();
        setXsol=new HashSet<>();
        addition=new HashMap<>();
    }

    public void addsys(String name,List <List<String>> sys){
        DSystem prblm = new DSystem(name, solutions, decisions,this);
        buildsys(prblm,sys);
        problems.add(prblm);
        countAtt=problems.get(0).getColumns().get(1).getLocaldomain().size();
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
            for(int i=0; i<problems.size(); i++){
                DSystem prblm = problems.get(i);
                Constraint c = new Constraint("D-system", new DPropagator(prblm));
                model.and(c).post();
            }
        }
        prepared=true;
        if(withtiming) java.lang.System.out.println("Preparation is done in "+(java.lang.System.currentTimeMillis()-time)+" ms");
    }

    public List<Variable> getActiveVars() {
        List<Variable> ret = new ArrayList<>();
        for (int i=0; i<vars.size(); i++)
            if(vars.get(i).isActive())ret.add(vars.get(i));
        return ret;
    }

    public int calcObjectWeight(List<Integer> varX){
        int res=0;
        for(int x:varX){
            res+=weights.get(x-1);
        }
        return res;
    }

    public long solvemany(boolean withtiming){
        preparechoco(withtiming);
        long time= java.lang.System.currentTimeMillis();
        Solver solver = model.getSolver();
        DVariableSelector varsel=new DVariableSelector(this);
        DValueSelector valsel=new DValueSelector(decisions);
        Decoperator decop = new Decoperator(decisions);
        solver.setSearch((intVarSearch(varsel,valsel,decop,problems.get(0).getIntVars())));
        while(solver.solve()){
            solutions.add(new Solution(vars,decisions.getDecisions()));
        }
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
        if(!justcount){
            for (Solution solution : solutions) {
                ret += solution.solutiontoString(false);
                if (withdec) ret += solution.decisionstoString();
                ret += "\n";
            }
        }
        return ret;
    }
    // фильтрация с помощью словаря
    public List<Solution> removeWastePattern(){
        List<Solution> filterSolutions=new ArrayList<>();
        if (solutions.size()==0) return  filterSolutions;
        uniqueSolution=new HashMap<>();
        for(Solution sol:solutions){
            String Xkey=sol.solution.get(0).toString(false);
            if(!uniqueSolution.containsKey(Xkey)){
                uniqueSolution.put(Xkey,sol);
            }
            else{
                Solution oldSol=uniqueSolution.get(Xkey);
                if(oldSol.getSolution().get(1).getValues().size()<sol.getSolution().get(1).getValues().size()) {
                    uniqueSolution.replace(Xkey,sol);
                }
            }
        }
        for(String s:uniqueSolution.keySet())
            filterSolutions.add(uniqueSolution.get(s));
        return  filterSolutions;
    }

   // рассчет числового вектора
    public void fillArrayLong(){
        List<Line> lines=problems.get(0).getLines();
        for(Line line:lines){
            List<Value> varX=line.getNodes().get(0).getValues();
            List<Value> varY=line.getNodes().get(1).getMissVals();
            for(Value x:varX){
                for(Value y:varY)
                    vectors[x.getValue()-1]+=(long)Math.pow(2,countAtt-y.getValue());
            }
        }
    }

    // фильтрация числовым вектором без сета
    public List<Solution> removeByVectorsNOSET(){
        List<Solution> filterSolutions=new ArrayList<>();
        for(Solution solution:solutions){
            List<Integer> valX=solution.solution.get(0).getValues();
            long vec=vectors[valX.get(0)-1];
            for(int i=1;i<valX.size();i++){
                vec=vec&vectors[valX.get(i)-1];
            }
            long solvec=getSolutionVector(solution);
            if (solvec==vec) filterSolutions.add(solution);
        }
        return filterSolutions;
    }

    // фильтрация числовым вектором с сетом
    public List<Solution> removeByVectorsWITHSET(){
        List<Solution> filterSolutions=new ArrayList<>();
        for (Solution solution : solutions) {
            String strVarx = solution.solution.get(0).toString(false);
            if(!setXsol.contains(strVarx)) {
                setXsol.add(strVarx);
                List<Integer> valX = solution.solution.get(0).getValues();
                long vec = vectors[valX.get(0) - 1];
                for (int i = 1; i < valX.size(); i++)
                    vec = vec & vectors[valX.get(i) - 1];
                char[] charVec=Long.toBinaryString(vec).toCharArray();
                List<Integer> newY=new ArrayList<>();
                long[] binaryNum=new long[charVec.length];
                for (int i=0;i<charVec.length;i++){
                    if (charVec[i]=='1') newY.add(i+1);
                }
                SolvedVariable svY=new SolvedVariable(newY,new Variable("Y"));
                List<SolvedVariable> sv=new ArrayList<>();
                sv.add(solution.solution.get(0));
                sv.add(svY);
                Solution sol=new Solution(sv);
                filterSolutions.add(sol);
            }

        }
        return filterSolutions;
    }

    //рассчет числового вектора для решения
    public long getSolutionVector(Solution sol){
        long res=0;
        List<Integer> varY=sol.solution.get(1).getValues();
        for(int i:varY) res+=(long) Math.pow(2,countAtt-i);
        return res;
    }

    // генерация списка числовых векторов
    public void generateArrayLong(){
        vectors=new long[problems.get(0).getColumns().get(0).getLocaldomain().size()];
        Arrays.fill(vectors,0);
    }
}