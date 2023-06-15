/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Myvariable {
    private final String name;
    private IntVar chocovar;
    private final List<Myvalue> domain;
    private final List<Column> columns;
    private boolean isUsed;

    public List<Myvalue> getDomain() {
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

    
    public void setChocovar(IntVar chocovar) {
        this.chocovar = chocovar;
    }
    public String getName() {
        return name;
    }

    public IntVar getChocovar() {
        return chocovar;
    }

    public Myvariable(String nam){
        name=nam;
        domain=new ArrayList<>();
        columns = new ArrayList<>();
    }

    public void addColumn(Column col){
        columns.add(col);
        col.setVariable(this);
    }
    
    public Myvariable(String nam,List<Myvalue> dom){
        name=nam;
        domain = dom;
        columns = new ArrayList<>();
        for(int i=0; i<dom.size(); i++)
            domain.get(i).setVariable(this);
    }
    
    public void addValue(Myvalue val){
        domain.add(val);
        val.setVariable(this);
    }

    private Myvalue getValue(CodedValue val){
        for(int i=0; i<domain.size(); i++)
            if(domain.get(i).getValue()==val)return domain.get(i);
        return null;
    }
    public Myvalue checkValue(CodedValue val){
        Myvalue ret = getValue(val);
        if(ret==null) {
            ret=new Myvalue(val);
            this.addValue(ret);
        }
        return ret;
    }
    public int[] toIntarr(){
        List<Integer> ret = new ArrayList<>();
        for(int i =0; i<domain.size(); i++)
            if(domain.get(i).isActive())ret.add(domain.get(i).getValue().getCode());
        int[] reti = new int[ret.size()];
        for (int i=0; i<ret.size(); i++)
            reti[i]=ret.get(i);
        return reti;
    }

    
    public void buildchoco(Model model){
        chocovar = model.intVar(name, toIntarr());
    }
    
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
            if(domain.get(i).isActive())ret+=domain.get(i).getValue().getValue()+";";
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
    
    public List<Myvalue> getActiveValues(){
        List<Myvalue> ret = new ArrayList<>();
        for(int i=0; i<domain.size(); i++)
            if(domain.get(i).isActive())ret.add(domain.get(i));
        return ret;
    }
}
