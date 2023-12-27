package GDSystem.UnarConstaints;

import GDSystem.Direction;
import GDSystem.Grid;
import GDSystem.Variable;

import java.util.Arrays;
import java.util.HashMap;

public class DistanceToWallConstraint extends UnarConstraints {
    Direction direction;
    int distance;
    Variable variable;
    Grid grid;
    public HashMap<Direction,int[][]> distanceToWallMap;

    @Override
    public void propagate(){

    }
    public DistanceToWallConstraint(Grid g){
        grid=g;
        distanceToWallMap=new HashMap<>();
        fillDTWMap();
    }
    public DistanceToWallConstraint(Direction direction,int distance){
        this.direction=direction;
        this.distance=distance;
    }
    public DistanceToWallConstraint(int distance){
        this.distance=distance;
    }

    public void fillDTWMap(){
        fillNorthDTW();
        fillSouthDTW();
        fillWestDTW();
        fillEastDTW();
    }
    public void fillNorthDTW(){
        int[][] matrix=new int[grid.height][grid.width];
        int j=-1;
        for(int x=0;x<matrix[0].length;x++){
            for(int y=0;y<matrix[0].length;y++){
                if (grid.grid[y][x]==-1||grid.grid[y][x]==2) {
                    matrix[y][x]=-1;
                    j=-1;
                }
                else matrix[y][x]=j;
                j++;
            }
            j=-1;
        }
        distanceToWallMap.put(Direction.NORTH,matrix);
        System.out.print("\n NORTH\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
    }

    public void fillSouthDTW(){
        int[][] matrix=new int[grid.height][grid.width];
        int j=-1;
        for(int x=matrix[0].length-1;x>=0;x--){
            for(int y=matrix.length-1;y>=0;y--){
                if (grid.grid[y][x]==-1||grid.grid[y][x]==2) {
                    matrix[y][x]=-1;
                    j=-1;
                }
                else matrix[y][x]=j;
                j++;
            }
        }
        distanceToWallMap.put(Direction.SOUTH,matrix);
        System.out.print("\n SOUTH\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
    }

    public void fillWestDTW(){
        int[][] matrix=new int[grid.height][grid.width];
        int j=-1;
        for(int y=0;y < matrix.length;y++){
            for(int x=0;x < matrix[0].length;x++){
                if(grid.grid[y][x]==-1||grid.grid[y][x]==2){
                    matrix[y][x]=-1;
                    j=-1;
                }
                else matrix[y][x]=j;
                j++;
            }
        }
        distanceToWallMap.put(Direction.WEST,matrix);
        System.out.print("\n WEST\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
    }

    public void fillEastDTW(){
        int[][] matrix=new int[grid.height][grid.width];
        int j=-1;
        for(int y=matrix.length-1;y>=0;y--){
            for (int x=matrix[0].length-1;x>=0;x--){
                if (grid.grid[y][x]==-1||grid.grid[y][x]==2) {
                    matrix[y][x]=-1;
                    j=-1;
                }
                else matrix[y][x]=j;
                j++;
            }
        }
        System.out.print("\n EAST\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
    }

}
