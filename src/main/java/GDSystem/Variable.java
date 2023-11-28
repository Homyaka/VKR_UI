package GDSystem;

import org.chocosolver.solver.variables.IntVar;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Variable {
    public String name;
    public Box box;
    public ArrayList<Integer> domain;
    public Cell[][] obj;

    public Variable(Box box){
        this.box=box;
        obj=new Cell[box.getHeight()][box.getWidth()];
        this.domain=new ArrayList<>();
    }
    public int getDomainSize(){
        return domain.size();
    }
   public String variableToString(){
        String s="";
        for(int i=0;i<obj.length;i++){
            for (int j=0;j<obj[0].length;j++){
                s+=obj[i][j]+" ";
            }
           s+="\n";
        }
        return s;
    }
   public String toString(){
        return name;
   }
}
