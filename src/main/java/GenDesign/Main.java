package GenDesign;

import GDSystem.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

public class Main {

    public static void testSolve(){
        Model testModel=new Model("test");
        IntVar v1= testModel.intVar("v1",new int[]{2,4,5,6,7,1});
        IntVar v2= testModel.intVar("v2",new int[]{3,4,5});
        IntVar[] testIntvar=new IntVar[2];
        testIntvar[0]=v1;
        testIntvar[1]=v2;
        Constraint c=new Constraint("testConstraint",new TestProp(testIntvar));
        testModel.and(c).post();
        org.chocosolver.solver.Solver solver=testModel.getSolver();
        TestVariableSelector variableSelector=new TestVariableSelector();
        TestValueSelector valueSelector=new TestValueSelector();
        solver.setSearch(intVarSearch(variableSelector,valueSelector,testIntvar));
        while (solver.solve()) {
            System.out.print(solver.getSolutionCount()+"sol:\n");
            System.out.print(testIntvar[0]+" "+testIntvar[1]+'\n');
        }
       // solver.findAllSolutions();
        System.out.print(solver.getSolutionCount()+"sols");
        //System.out.print("\n"+testIntvar[0]+" "+testIntvar[1]);
    }
    public static void main(String[] args) {
        GenApplication.main(args);
    }



}
