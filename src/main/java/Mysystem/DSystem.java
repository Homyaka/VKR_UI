/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class DSystem extends Activable{
    private final List<MySolution> solutions;
    private final Decisions decisions;
    private final String name;
    private final List<Line> lines;
    private final List<Column> columns;
   // private final boolean isC;
    
    @Override
    void onActivate(){}

    
    public Column getColumn(String var){ // получить столбец по заданной переменной
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).getVariable().getName().compareTo(var)==0)return columns.get(i);
        return null;
    }
    public int getActivelinescount() { //получить кол-во активных строк в системе
        int count=0;
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())count++;
        return count;
    }

    public Decisions getDecisions() {
        return decisions;
    }

    public int getActivecolumnscount() { //получить кол-во активных столбцов в системе
        int count=0;
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())count++;
        return count;
    }
    
    public List<Column> getActivecolumns() { //получить лист активных столбцов в системе
        List<Column> ret=new ArrayList<>();
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())ret.add(columns.get(i));
        return ret;
    }
    public DSystem(String nam, List<MySolution> sols, Decisions decs){
        name=nam;
        lines=new ArrayList<>();
        columns = new ArrayList<>();
        solutions=sols;
        decisions=decs;
    }


    public List<MySolution> getSolutions() {
        return solutions;
    }


    public String getName() {
        return name;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Column> getColumns() {
        return columns;
    }

    /*public boolean isC() {
        return isC;
    }*/
    
    public void addColumn(Column var){ //добавить в систему столбец
        columns.add(var);
        var.setSystem(this);
    }

    public void addLine(Line lin){  //добавить в систему строку
        lines.add(lin);
        lin.setSystem(this);
    }

    @Override
    public String toString(){
        String ret="";
        for(int i=0; i<columns.size(); i++)
            ret=ret+columns.get(i).toString()+" ";
        ret=ret+"\n";
        
        
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())
                ret=ret+lines.get(i).toString()+"\n";
        return ret;
    }
    
    public String printChoco(){
        String ret="";
        for(int i=0; i<columns.size(); i++)
            ret=ret+columns.get(i).getVariable().getChocovar().toString()+" ";
        ret=ret+"\n";
        return ret;
    }

    public IntVar[] getIntvars(){  // получить массив переменных IntVar
        List<IntVar> ret = new ArrayList<>();
        for(int i=0; i<this.getColumns().size(); i++)
            ret.add(this.getColumns().get(i).getVariable().getChocovar());
        return ret.toArray(new IntVar[ret.size()]);
    }

    public List<Line> getActivelines(){ // //получить лист активных строк в системе
        List<Line> ret=new ArrayList<>();
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())ret.add(lines.get(i));
        return ret;
    }
    
    /*@Override
    public boolean isActive(){
    return (getActivecolumnscount()!=0)&&(getActivelinescount()!=0);
    }*/
}
