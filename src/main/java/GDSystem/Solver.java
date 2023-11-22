package GDSystem;

import java.util.List;

public class Solver {
    public List<Variable> variables;
    public Grid grid;

    public Solver(List<Variable> variables,Grid grid){
        this.variables=variables;
        this.grid=grid;
    }
    public boolean tryPlace(Variable v,int point){
        for(int i=0;i<v.obj.length;i++){
            for(int j=0;j<v.obj[0].length;j++){
                if (grid.fromIntToCell(point+j+grid.width*i)!=Cell.EMPTY) return false;
            }
        }
        return true;
    }

    public void place(Variable v,int point){
        for (int i=0;i<v.obj.length;i++){
            for(int j=0;j<v.obj[0].length;j++){
                int newpoint=point+i*grid.width+j;
                System.out.print("p: "+newpoint);
                int[] p= grid.getPointCell(newpoint);
                grid.grid[p[1]][p[0]]=v.obj[i][j];
            }
        }
    }

    public void computeDomains(){
        for(Variable v:variables){
            computeDomain(v);
        }
    }
    public void computeDomain(Variable variable){
        for(int i=0;i<grid.points.length;i++){
            if(tryPlace(variable,i)) variable.domain.add(i);
        }
    }
}
