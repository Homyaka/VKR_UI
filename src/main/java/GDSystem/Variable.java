package GDSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Variable {
    private Box box;
    public List<Integer> domain;
    public Cell[][] obj;

    public Variable(Box box){
        this.box=box;
        obj=new Cell[box.getHeight()][box.getWidth()];
        this.domain=new ArrayList<>();
    }

   public String toString(){
        String s="";
        for(int i=0;i<obj.length;i++){
            for (int j=0;j<obj[0].length;j++){
                s+=obj[i][j]+" ";
            }
           s+="\n";
        }
        return s;
    }
}
