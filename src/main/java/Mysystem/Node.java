package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Node extends Activable{
    private Column column;
    private Line line;
    private List<Value> values;
    @Override
    void onActivate(){
    }
    private boolean isActive(boolean check){
        return line.isActive()&& column.isActive();
    }
    public Column getColumn() {
        return column;
    }

    public Line getLine() {
        return line;
    }

    public List<Value> getValues() {
        return values;
    }

    public int getActive() {
        int count=0;
        for(int i=0; i<values.size(); i++)
            if(values.get(i).isActive())count++;
        return count;
    }

    public Node(List<Value> vals){
        values = vals;
    }

    public void setColumn(Column variable) {
        this.column = variable;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @Override
    public String toString(){
        String ret="";
        if(this.isActive(true)){
            ret="{";
            for (Value value : values)
                if (value.isActive())
                    ret += value.getValue() + ";";
            ret+="}";}
        return ret;
    }
    
    public List<Activable> getComplvals(){
        List<Activable> ret = new ArrayList<>();
        for(int i = 0; i< column.getLocaldomain().size(); i++)
            if(column.getLocaldomain().get(i).isActive()&&!values.contains(column.getLocaldomain().get(i)))
                ret.add(column.getLocaldomain().get(i));
        return ret;
    }

    public List<Value> getMissVals(){
        List<Value> ret = new ArrayList<>();
        for(int i = 0; i< column.getLocaldomain().size(); i++)
            if(column.getLocaldomain().get(i).isActive()&&!values.contains(column.getLocaldomain().get(i)))
                ret.add(column.getLocaldomain().get(i));
        return ret;
    }
    
    public List<Value> getActivevals(){
        List<Value> ret = new ArrayList<>();
        for (Value value : values)
            if (value.isActive())
                ret.add(value);
        return ret;
    }
}
