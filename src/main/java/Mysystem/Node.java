/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Node extends Activable{
    private Column column; //колонка
    private Line line; //строка
    private List<Value> values; //значения в ячейке
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

    public int getActive() { //Count количество активных значений
        int count=0;
        for(int i=0; i<values.size(); i++)
            if(values.get(i).isActive())count++;
        return count;
    }
    
    public Node(){
        values = new ArrayList<>();
    }

    public Node(List<Value> vals){
        values = vals;
    }
    
    public void addval(Value val){
        values.add(val);
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
            for(int i =0; i<values.size(); i++)
                if(values.get(i).isActive())ret+=values.get(i).getValue().getValue()+";";
            ret+="}";}
        //ret+="active: "+active;
        return ret;
    }
    
    public int[] toIntarr(){
        List<Integer> ret = new ArrayList<>();
        for(int i =0; i<values.size(); i++)
            if(values.get(i).isActive())ret.add(values.get(i).getValue().getCode());
        int[] reti = new int[ret.size()];
        for (int i=0; i<ret.size(); i++)
            reti[i]=ret.get(i);
        return reti;
    }
    
    public List<Activable> getComplvals(){ //получить значения домена столбца которых нет в этой ячейке (активные)
        List<Activable> ret = new ArrayList<>();
        for(int i = 0; i< column.getLocaldomain().size(); i++)
            if(column.getLocaldomain().get(i).isActive()&&!values.contains(column.getLocaldomain().get(i)))
                ret.add(column.getLocaldomain().get(i));
        return ret;
    }
    
    public List<Value> getActivevals(){  // получить активные значения ячейки
        List<Value> ret = new ArrayList<>();
        for(int i=0; i<values.size(); i++)
            if(values.get(i).isActive())
                ret.add(values.get(i));
        return ret;
    }

}
