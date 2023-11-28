package GDSystem;

import org.chocosolver.solver.Model;

import java.security.PublicKey;
import java.util.*;

public class Solver {
    public Model model;
    public List<Variable> variables;
    public Grid grid;
    public List<Variable> unarrangedVariables;
    public HashMap<Variable,Integer> arrangedVariables;
    public GDPropagator propagator;
    public List<Grid> changedGrids;

    public Solver(List<Variable> variables,Grid grid){
        this.variables=variables;
        this.grid=grid;
        propagator=new GDPropagator(this);
        unarrangedVariables=new ArrayList<>();
        arrangedVariables=new HashMap<>();
    }

    public void fillArrayUnarrangedVariables(){
        unarrangedVariables.addAll(variables);
        unarrangedVariables.sort(Comparator.comparing(Variable::getDomainSize));
        System.out.print("Sort by domain size:");
        for(Variable v:unarrangedVariables) System.out.print(v.getDomainSize()+" ");
    }
    public boolean tryPlace(Variable v,int point){
        for(int i=0;i<v.obj.length;i++){
            for(int j=0;j<v.obj[0].length;j++){
                if (grid.fromIntToCell(point+j+grid.width*i)!=Cell.EMPTY) return false;
            }
        }
        return true;
    }

    public Solution solve(){
        Solution solution=new Solution();
        while(unarrangedVariables.size()!=0){
            int i=0;
            Variable settingVar=unarrangedVariables.get(i);
            if(settingVar.domain.size()!=0) {
                solution.placedVariables.put(settingVar,settingVar.domain.get(0));
                place(settingVar, settingVar.domain.get(0));
                i++;
            }
            if(settingVar.domain.size()==0){
                if (i==0) {
                    System.out.print("Решений не найдено!!!");
                    return null;
                }
            }
        }
        /*for(Variable v:variables){
            System.out.print(v.domain.toString()+"\n");
            solution.placedVariables.put(v,v.domain.get(0));
            place(v,v.domain.get(0));
        }*/
        return solution;
    }

    public void place(Variable v,int point){
        for (int i=0;i<v.obj.length;i++){
            for(int j=0;j<v.obj[0].length;j++){
                int newpoint=point+i*grid.width+j;
                int[] p= grid.getPointCell(newpoint);
                grid.grid[p[1]][p[0]]=v.obj[i][j];
            }
        }
        arrangedVariables.put(v,point);
        int index=unarrangedVariables.indexOf(v);
        if(index!=-1) unarrangedVariables.remove(index);
        propagator.arrangedVariable=v;
        propagator.point=point;
        propagator.reComputeDomains();
    }


    public void computeDomains(){
        for(Variable v:variables){
            computeDomain(v);
        }
        fillArrayUnarrangedVariables();
    }
    public void computeDomain(Variable variable){
        for(int i=0;i<grid.points.length;i++){
            if(tryPlace(variable,i)) variable.domain.add(i);
        }
    }
}
