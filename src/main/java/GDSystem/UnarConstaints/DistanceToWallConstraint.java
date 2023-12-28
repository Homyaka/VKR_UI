package GDSystem.UnarConstaints;

import GDSystem.Direction;
import GDSystem.Grid;
import GDSystem.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DistanceToWallConstraint extends UnarConstraints {
    public String type;
    public Direction direction;
    public int distance;
    public Variable variable;
    public Grid grid;
    public int[][] matrix;
    @Override
    public void propagate(){
        System.out.println("Domain");
        for(int i:variable.domain) System.out.print(i+" ");
        System.out.println("\nMap "+direction);
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
            ArrayList<Integer> removeList=new ArrayList<>();
            for(int i=0;i<variable.domain.size();i++){
                int[] p= grid.getPointCell(variable.domain.get(i));
                if(matrix[p[1]][p[0]]!=distance) {
                    System.out.println("\n"+Arrays.toString(p));
                    removeList.add(variable.domain.get(i));
                }
            }
            variable.domain.removeAll(removeList);
        }
    public DistanceToWallConstraint(Variable variable,Direction direction,int distance){
        super("DTW");
        this.variable=variable;
        this.direction=direction;
        this.distance=distance;
    }

    public DistanceToWallConstraint(Variable variable,Direction direction,int distance,int[][] matrix) {
        super("DTW");
        this.matrix=matrix;
        this.variable = variable;
        this.distance = distance;
        this.direction = direction;
    }
}
