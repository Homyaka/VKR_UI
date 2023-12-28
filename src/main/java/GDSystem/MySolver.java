package GDSystem;

import GDSystem.UnarConstaints.DistanceToWallConstraint;
import GDSystem.UnarConstaints.UnarConstraints;
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
    public HashMap<Direction,int[][]> distanceToWallMap;


    public MySolver(List<Variable> variables, Grid grid){
        model=new Model(" ");
        intVars=new IntVar[variables.size()];
        filledGrids=new ArrayList<>();
        this.variables=variables;
        this.grid=grid;
        fillDTWMap();
        prepared=false;
        computeDomains();
        solutions=new ArrayList<>();
    }

    public void fillDTWMap(){
        distanceToWallMap=new HashMap<>();
        fillNorthDTW();
        fillSouthDTW();
        fillWestDTW();
        fillEastDTW();
        fillDTAnyW();
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
        distanceToWallMap.put(Direction.EAST,matrix);
        System.out.print("\n EAST\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
    }
    public void fillDTAnyW() {
        int[][] matrix = new int[grid.height][grid.width];
        int[][] north = distanceToWallMap.get(Direction.NORTH);
        int[][] south = distanceToWallMap.get(Direction.SOUTH);
        int[][] west = distanceToWallMap.get(Direction.WEST);
        int[][] east = distanceToWallMap.get(Direction.EAST);
        for(int y=0;y < matrix.length;y++){
            for(int x=0;x < matrix[0].length;x++){
                int min=Integer.min(east[y][x],Integer.min(west[y][x],Integer.min(north[y][x],south[y][x])));
                matrix[y][x]=min;
            }
        }
        distanceToWallMap.put(Direction.ANY,matrix);
        System.out.print("\n ANY\n");
        for(int q=0;q<matrix.length;q++){
            System.out.println(Arrays.toString(matrix[q]));
        }
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
            if (v.unarConstraints!=null) {
                for (UnarConstraints constraint : v.unarConstraints) {
                   System.out.print("IN COMP DOMAIN");
                   if(constraint.type.equals("DTW")){
                       DistanceToWallConstraint temp= (DistanceToWallConstraint) constraint;
                       DistanceToWallConstraint constr= new DistanceToWallConstraint(v,temp.direction,temp.distance,distanceToWallMap.get(temp.direction));
                       constr.grid=this.grid;
                       constr.propagate();
                    }
                }
            }
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
        return java.lang.System.currentTimeMillis()-time;
    }

}
