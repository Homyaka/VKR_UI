package GDSystem;

import GDSystem.UnarConstaints.DistanceToWallConstraint;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

public class MySolver {
    public Model model;
    public List<Variable> variables;
    public Grid grid;
    public Grid filledGrid;
    public Boolean prepared;
    public IntVar[] intVars;
    public List<Grid> filledGrids;
    public List<Solution> solutions;

    public MySolver(List<Variable> variables, Grid grid){
        model=new Model(" ");
       // model.getSolver().limitTime(Long.MAX_VALUE);
        intVars=new IntVar[variables.size()];
        filledGrids=new ArrayList<>();
        this.variables=variables;
        this.grid=grid;
        prepared=false;
        computeDomains();
        solutions=new ArrayList<>();
    }


    public boolean tryPlace(Variable v,int point){
        for(int i=0;i<v.obj.length;i++){
            for(int j=0;j<v.obj[0].length;j++){
                if (grid.fromIntToCell(point+j+grid.width*i)!=0) return false;
            }
        }
        return true;
    }

    public void computeDomains(){
        for(Variable v:variables){
            v.domain.clear();
            computeDomain(v);
        }
        variables.sort(Comparator.comparing(Variable::getDomainSize));
        for(int i=0;i<variables.size();i++){
            intVars[i]=model.intVar(variables.get(i).name,variables.get(i).convertDomainFromListToArray());
            variables.get(i).intVar=intVars[i];
        }
    }
    public void computeDomain(Variable variable){
        for(int i=0;i<grid.points.length;i++){
            if(tryPlace(variable,i)) variable.domain.add(i);
        }
    }
    private void preparechoco(boolean withtiming){
        long time= java.lang.System.currentTimeMillis();
        if(!prepared){
            Constraint c = new Constraint("GDConstraint", new GDPropagator(intVars,this));
            model.and(c).post();
            prepared=true;
        }
        if(withtiming)
            System.out.print("Preparation is done in "+(java.lang.System.currentTimeMillis()-time)+" ms\n");
    }

    public long manySolve(boolean withtiming){
        preparechoco(withtiming);
        long time=java.lang.System.currentTimeMillis();
        org.chocosolver.solver.Solver solver=model.getSolver();
        GDVariableSelector variableSelector=new GDVariableSelector();
        GDValueSelector valueSelector=new GDValueSelector();
        solver.setSearch((intVarSearch(variableSelector,valueSelector,intVars)));
        while (solver.solve()) {
            Solution sol=new Solution(variables);
            solutions.add(sol);
        }
        System.out.print("\nВсего решений: "+solver.getSolutionCount());
        DistanceToWallConstraint distance=new DistanceToWallConstraint(grid);
        return java.lang.System.currentTimeMillis()-time;
    }

}
