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
    public static void main(String[] args) throws IOException, ContradictionException {
       // testSolve();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        /*Variable var1=new Variable(new Box(4,4));
        var1.obj=new Cell[][]{{Cell.FREE,Cell.FREE,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT,Cell.OBJECT}};
        Variable var2= new Variable(new Box(3,3));
        var2.obj=new Cell[][]{{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT}};
        Variable var3=new Variable(new Box(3,2));
        var3.obj= new Cell[][]{{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT,Cell.OBJECT}};*/
        List<Variable> vs=new ArrayList<>();
        Variable var1=new Variable(new Box(2,2));
        Variable var2= new Variable(new Box(2,1));
        var1.obj=new Cell[][]{{Cell.OBJECT,Cell.OBJECT},{Cell.OBJECT,Cell.OBJECT}};
        var2.obj= new Cell[][]{{Cell.OBJECT,Cell.OBJECT}};
        vs.add(var1);
        vs.add(var2);
        var1.name="var1";
        var2.name="var2";
        //создание грида
        System.out.print("width: ");
        int width =Integer.parseInt(reader.readLine());
        System.out.print("height: ");
        int height =Integer.parseInt(reader.readLine());
        System.out.print("\n");
        Grid grid=new Grid(width,height);
        for(int i=0;i<grid.grid.length;i++)
            Arrays.fill(grid.grid[i],Cell.EMPTY);
        Arrays.fill(grid.grid[0],Cell.WALLS);
        Arrays.fill(grid.grid[grid.grid.length-1],Cell.WALLS);
        for(int i=1;i<grid.grid.length-1;i++){
            grid.grid[i][0]=Cell.WALLS;
            grid.grid[i][grid.grid[0].length-1]=Cell.WALLS;
        }
        //grid.grid[2][1]=Cell.WALLS;
        //System.out.print(grid);
        //System.out.println("\n"+grid.pointsToGridString());
        // создание решателя и распространителя
        MySolver mySolver =new MySolver(vs,grid);
        mySolver.firstSolve(true);
    }


}
