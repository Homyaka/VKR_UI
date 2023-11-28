package Mysystem;

import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class Variable  {
    private final String name;

    private final List<Value> domain;
    private final List<Column> columns;

    public List<Value> getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public Variable(String nam){
        name=nam;
        domain=new ArrayList<>();
        columns = new ArrayList<>();
    }
    public void addColumn(Column col){
        columns.add(col);
        col.setVariable(this);
    }
    public Variable(String nam, List<Value> dom){
        name=nam;
        domain = dom;
        columns = new ArrayList<>();
        for(int i=0; i<dom.size(); i++)
            domain.get(i).setVariable(this);
    }
    public void addValue(Value val){
        domain.add(val);
        val.setVariable(this);
    }
    private Value getValue(int val){
        for (Value value : domain)
            if (value.getValue() == val)
                return value;
        return null;
    }
    public Value checkValue(int val){
        Value ret = getValue(val);
        if(ret==null) {
            ret=new Value(val);
            this.addValue(ret);
        }
        return ret;
    }

    @Override
    public String toString(){
        String ret = name+" = {";
        for (Value value : domain)
            if (value.isActive())
                ret += value.getValue() + ";";
        ret=ret.substring(0, ret.length()-1)+"}";
        return ret;
    }

    public boolean isActive(){
        for (Column column : columns)
            if (column.isActive())
                return true;
        return false;
    }
}
