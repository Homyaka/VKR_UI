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
import workWithFiles.DataWorker;

public class Problem {
    //public HashMap<SolvedVariable,SolvedVariable> uniqueSolution;
    public HashMap<String,Solution> uniqueSolution;
    private final Model model;
    private final List<Variable> vars;
    private final HashSet<Integer> domvals;
    private List<DSystem> problems;
    private int minSup;

    public int containAtt;
    public int noContainAtt;
    public int patternLength;
    public HashMap<IntVar,Line> mapIntVarLine;

    public HashSet<String> setXsol;

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
        System.out.println(weight);
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
        // System.out.println(prblm.toString());
       // System.out.println(prblm.getLines().size()+"  "+prblm.getColumns().size());
    }
    
    public void buildqueen(int n){
       /* Nqueens nq = new Nqueens();
        nq.build(n, vars, domvals, problems, solutions, decisions);*/
    }
    public Problem(String name,int minSup){
        mapIntVarLine =new HashMap<>();
        this.minSup=minSup;
        containAtt=-1;
        noContainAtt=-1;
        patternLength=-1;
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
    }

    public Problem(String name,int minSup,int containAtt,int noContainAtt,int patternLength){
        mapIntVarLine =new HashMap<>();
        this.minSup=minSup;
        this.containAtt=containAtt;
        this.noContainAtt=noContainAtt;
        this.patternLength=patternLength;
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
    public List<Solution> removeWastePattern(){
        List<Solution> filterSolutions=new ArrayList<>();
        if (solutions.size()==0) return  filterSolutions;
        uniqueSolution=new HashMap<>();
        for(Solution sol:solutions){
            String Xkey=sol.solution.get(0).toString(false);
            if(!uniqueSolution.containsKey(Xkey)){
                uniqueSolution.put(Xkey,sol);
                System.out.println("add "+Xkey);
            }
            else{
                Solution oldSol=uniqueSolution.get(Xkey);
                if(oldSol.getSolution().get(1).getValues().size()<sol.getSolution().get(1).getValues().size()) {
                    uniqueSolution.replace(Xkey,sol);
                    System.out.println("replace "+Xkey);
                }
            }
        }
        for(String s:uniqueSolution.keySet())
            filterSolutions.add(uniqueSolution.get(s));
        return  filterSolutions;
    }

    public void genBoolVectors() {
       /* Variable X=vars.get(0);
        List<Value> values=X.getDomain();*/
        List<Line> lines = problems.get(0).getLines();
        for (Line line : lines) {
            List<Value> Ynode = line.getNodes().get(1).getMissVals();
            List<Value> Xnode = line.getNodes().get(0).getActivevals();
            List<Integer> Yvals = new ArrayList<>();
            List<Integer> Xvals = new ArrayList<>();
            for (Value x : Xnode) Xvals.add(x.getValue());
            for (Value y : Ynode) Yvals.add(y.getValue());
            for (int i : Xvals) System.out.print(i + " ");
            System.out.print("\t Y: ");
            for (int i : Yvals) System.out.print(i + " ");
            System.out.print("\n");
            Boolean[] test;
            for (Value x : Xnode) {
                for (Value y : Ynode) {
                    listBoolVectors.get(x.getValue() - 1)[y.getValue() - 1] = true;
                }
            }
        }
    }
    public List<Solution> removeByBooleanVector(){
        int num=1;
        List<Solution> filterSolutions=new ArrayList<>();
        for(Solution solution:solutions) {
            String strVarx = solution.solution.get(0).toString(false);
            if (!setXsol.contains(strVarx)) {
                setXsol.add(strVarx);
                List<Integer> varX = solution.solution.get(0).getValues();
                Boolean[] res = listBoolVectors.get(varX.get(0) - 1);
                Boolean[] solVector = getBoolVectorSolution(solution);
                boolean flag = false;
                for (int i = 1; i < varX.size(); i++) {
                    res = vectorMultiply(res, listBoolVectors.get(varX.get(i) - 1));
                }
                List<Integer> Y=new ArrayList<>();
                for (int i=0;i<res.length;i++){
                    if (res[i]) Y.add(i+1);
                }
                SolvedVariable newY=new SolvedVariable(Y,solution.getSolution().get(1).getVariable());
                List<SolvedVariable> sol=new ArrayList<>();
                sol.add(solution.solution.get(0));
                sol.add(newY);
                filterSolutions.add(new Solution(sol));
               // if (checkVectors(res,solVector)) filterSolutions.add(solution);
            }
        }
        return filterSolutions;
    }

    public void fillArrayLong(){
        List<Line> lines=problems.get(0).getLines();
        for(Line line:lines){
            List<Value> varX=line.getNodes().get(0).getValues();
            List<Value> varY=line.getNodes().get(1).getMissVals();
            for(Value y: varY) System.out.println("\n In Y: "+y.getValue());
            for(Value x:varX){
                for(Value y:varY)
                    vectors[x.getValue()-1]+=(long)Math.pow(2,countAtt-y.getValue());
            }
        }
        System.out.println("Check BoolVector and LongVector:");
        for(int i=0;i<vectors.length;i++){
            System.out.print("Boolean: ");
            Boolean[] booleans=listBoolVectors.get(i);
            for(Boolean b:booleans) System.out.print(b+" ");
            System.out.print("    Long: "+vectors[i]);
            System.out.print("\n");
        }
    }

    public List<Solution> removeByVectorsNOSET(){
        List<Solution> filterSolutions=new ArrayList<>();
        for(Solution solution:solutions){
            List<Integer> valX=solution.solution.get(0).getValues();
            long vec=vectors[valX.get(0)-1];
            System.out.print("X: ");
            for (int x:valX) System.out.print(x+" ");
            System.out.println('\n');
            System.out.println("LongVector: "+vec);
            System.out.print("BoolVector: ");
            Boolean[] b=listBoolVectors.get(valX.get(0)-1);
            for(boolean q:b) System.out.print(q+" ");
            System.out.print("\n");
            for(int i=1;i<valX.size();i++){
                vec=vec&vectors[valX.get(i)-1];
                System.out.println(vec);
            }
            long solvec=getSolutionVector(solution);
            System.out.println("Compare Vec and SolVec:");
            System.out.println(vec);
            System.out.println(solvec);
            if (solvec==vec) filterSolutions.add(solution);
        }
        return filterSolutions;
    }
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

    public long getSolutionVector(Solution sol){
        long res=0;
        List<Integer> varY=sol.solution.get(1).getValues();
        for(int i:varY) res+=(long) Math.pow(2,countAtt-i);
        return res;
    }
    public void generateListBoolVector(){
        for(int i=0;i<problems.get(0).getColumns().get(0).getLocaldomain().size();i++){
            Boolean[] vec=new Boolean[problems.get(0).getColumns().get(1).getLocaldomain().size()];
            Arrays.fill(vec,false);
            listBoolVectors.add(vec);
        }
    }
    public void generateArrayLong(){
        vectors=new long[problems.get(0).getColumns().get(0).getLocaldomain().size()];
        Arrays.fill(vectors,0);
    }
    public void generateBoolVectorsList( File oatableFile) throws IOException {
        if(oatableFile.isFile()){
            DataWorker dw=new DataWorker();
            dw.generatematrix(dw.txtParse(oatableFile.getPath()));
            listBoolVectors=new ArrayList<>();
            for(int n=0;n<dw.matrix.length;n++)
                listBoolVectors.add(dw.matrix[n]);
        }
        else {
            genBoolVectors();
            /*System.out.print("НЕ ТОТ ПУТЬ ФАЙЛА");
            int yDomSize;
            String l = text.get(1).substring(1, text.get(1).length());
            yDomSize = l.split(",").length;
            List<Value> values = vars.get(0).getDomain();
            for (int i = 3; i < text.size(); i++) {
                String line = text.get(i);
                String[] str = line.split(" ");
                String node = str[0];
                node = node.substring(1, node.length());
                String y = str[0].substring(1, str.length);
                int ind = getMissingNum(y, yDomSize);
                for (String s : node.split(",")) {
                    System.out.print(s+" ");
                }
                System.out.print('\n');*/
            }
        }
    public Boolean[] getBoolVectorSolution(Solution sol){
        Boolean[] res=new Boolean[listBoolVectors.get(0).length];
        Arrays.fill(res,false);
        List<Integer> varY=sol.solution.get(1).getValues();
            for(int y:varY){
                res[y-1]=true;
            }
            System.out.println('\n'+"SOLUTION");
        for(int i=0;i<res.length;i++){
            System.out.print(res[i]+" ");
        }
        System.out.println('\n');
        return res;
    }
    public int getMissingNum(String str,int size){
        for(int i=1;i<=size;i++){
            String i_= String.valueOf(i);
            if(!str.contains(i_)) return i;
        }
        return -1;
    }
    public Boolean[] vectorMultiply(Boolean[] arr1,Boolean[] arr2){
        Boolean[] res=new Boolean[arr1.length];
        for (boolean b : arr1) System.out.print(b + " ");
        System.out.print('\n');
        for (boolean b : arr2) System.out.print(b + " ");
        for(int i=0;i<arr1.length;i++)
            res[i]=arr1[i]&arr2[i];
        System.out.print('\n');
        for (boolean re : res) System.out.print(re + " ");
        System.out.print('\n');
        System.out.print('\n');
        return res;
    }
    public boolean checkVectors(Boolean[] arr1,Boolean[] arr2){
        boolean flag=true;
        for(int i=0;i<arr1.length;i++)
            System.out.print(arr1[i]+" ");
        System.out.print("\n");
        for(int i=0;i<arr2.length;i++)
            System.out.print(arr2[i]+" ");
        System.out.print("\n");
        for(int i=0;i<arr1.length;i++)
            if (arr1[i]!=arr2[i]) flag=false;
        System.out.print(flag);
        return flag;
    }
}
