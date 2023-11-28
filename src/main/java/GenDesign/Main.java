package GenDesign;

import GDSystem.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //создание переменных
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
        var1.name="1";
        var2.name="2";
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
        System.out.print(grid);
        System.out.println("\n"+grid.pointsToGridString());
        // создание решателя и распространителя
        Solver solver=new Solver(vs,grid);
        solver.computeDomains();
        //тест
        System.out.print(" Before\n"+grid);
        System.out.print("domain var2 before: ");
        for(int i:var2.domain) System.out.print(i+" ");
        System.out.print("\ndomain var3 before: ");
      //  for(int i: var3.domain) System.out.print(i+" ");
        //solver.place(var1,11);
        System.out.print("\n\ndomain var2 after: ");
        for(int i:var2.domain) System.out.print(i+" ");
        System.out.print("\ndomain var3 after: ");
       // for(int i:solver.variables.get(2).domain) System.out.print(i+" ");
        System.out.print(solver.solve());
    }
}
