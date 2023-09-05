/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Variable {
    private final String name;
    //private IntVar chocovar;
    private final List<Value> domain;
    private final List<Column> columns;
    private boolean isUsed;

    public List<Value> getDomain() {
        return domain;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }

    
   /*public void setChocovar(IntVar chocovar) {
        this.chocovar = chocovar;
    }*/
    public String getName() {
        return name;
    }

    /*public IntVar getChocovar() {
        return chocovar;
    }*/

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
        for(int i=0; i<domain.size(); i++)
            if(domain.get(i).getValue()==val)return domain.get(i);
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
    public int[] toIntarr(){
        List<Integer> ret = new ArrayList<>();
        for(int i =0; i<domain.size(); i++)
            if(domain.get(i).isActive())ret.add(domain.get(i).getValue());
        int[] reti = new int[ret.size()];
        for (int i=0; i<ret.size(); i++)
            reti[i]=ret.get(i);
        return reti;
    }

    
    /*public void buildchoco(Model model){
      //  chocovar = model.intVar(name, toIntarr());
    }*/
    
    public int getActiveDomainSize(){
        int ret=0;
        for(int i=0; i<domain.size(); i++)
            if(domain.get(i).isActive())ret++;
        return ret;
    }
    
    @Override
    public String toString(){
        String ret = name+" = {";
        for(int i=0; i<domain.size();i++)
            //if(domain.get(i).isActive())ret+=domain.get(i).getValue().getValue()+";";
            if(domain.get(i).isActive())ret+=domain.get(i).getValue()+";";
        ret=ret.substring(0, ret.length()-1)+"}";
        return ret;
    }

    //new------
    public boolean isActive(){
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())return true;
        return false;
    }
    public int getConectionnumber(){
        int ret=0;
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())ret+=columns.get(i).getActivenodescount();
        return ret;
    }
    public List<Column> getActiveColumns(){
        List<Column> ret = new ArrayList<>();
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())ret.add(columns.get(i));
        return ret;
    }
    
    public List<Value> getActiveValues(){
        List<Value> ret = new ArrayList<>();
        for(int i=0; i<domain.size(); i++)
            if(domain.get(i).isActive())ret.add(domain.get(i));
        return ret;
    }
}
