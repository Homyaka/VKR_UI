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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Variable var1=new Variable(new Box(2,2));
        var1.obj=new Cell[][]{{Cell.FREE,Cell.FREE},{Cell.OBJECT,Cell.OBJECT}};
        Variable var2= new Variable(new Box(1,2));
        var2.obj=new Cell[][]{{Cell.FREE},{Cell.OBJECT}};
        System.out.print(var1);
        System.out.print("\n");
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
        System.out.print(grid);
        System.out.println("\n"+grid.pointsToGridString());
        System.out.println(grid.fromCellToInt(0,3));
        System.out.println(grid.fromIntToCell(19));
        List<Variable> vs=new ArrayList<>();
        vs.add(var1);
        vs.add(var2);
        Solver solver=new Solver(vs,grid);
        System.out.println(solver.tryPlace(var1,6));
        solver.computeDomains();
        System.out.print("domain var1: ");
        for(int i:var1.domain) System.out.print(i+" ");
        System.out.print("\ndomain var2: ");
        for (int i: var2.domain) System.out.print(i+" ");
        //grid.getPointCell(10);
        solver.place(var1,15);
        System.out.print("BEFORE:\n"+grid);
        System.out.print("RESULT:\n");
        System.out.print(grid);
    }
}
