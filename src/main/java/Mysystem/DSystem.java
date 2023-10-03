/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.variables.IntVar;

public class DSystem extends Activable{

    private final Decisions decisions;
    private final String name;
    private final List<Line> lines;
    private final List<Column> columns;

    private Problem problem;

    public DSystem(String name, List<Solution> sols, Decisions decs, Problem problem){
        this.name =name;
        lines=new ArrayList<>();
        columns = new ArrayList<>();
        decisions=decs;
        this.problem=problem;
    }
    @Override
    void onActivate(){}

    public int getActiveLinesCount() { //получить кол-во активных строк в системе
        int count=0;
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())count++;
        return count;
    }

    public int getActiveColumnsCount() { //получить кол-во активных столбцов в системе
        int count=0;
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())count++;
        return count;
    }

    public void addColumn(Column column){ //добавить в систему столбец
        columns.add(column);
        column.setSystem(this);
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
    public IntVar[] getIntVars(){  // получить массив переменных IntVar
        List<IntVar> ret = new ArrayList<>();
        for (Line line : lines) ret.add(line.intVar);
        return ret.toArray(new IntVar[ret.size()]);
    }
    @Override
    public boolean isActive(){
    return (getActiveColumnsCount()!=0)&&(getActiveLinesCount()!=0);
    }

    public Decisions getDecisions() {
        return decisions;
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

    public Problem getProblem() {return problem;}

    /*public String printChoco(){
        String ret="";
        for(int i=0; i<columns.size(); i++)
            ret=ret+columns.get(i).getVariable().getChocovar().toString()+" ";
        ret=ret+"\n";
        return ret;
    }*/

    /*public List<Column> getActivecolumns() { //получить лист активных столбцов в системе
        List<Column> ret=new ArrayList<>();
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())ret.add(columns.get(i));
        return ret;
    }*/

     /* public Column getColumn(String var){ // получить столбец по заданной переменной
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).getVariable().getName().compareTo(var)==0)return columns.get(i);
        return null;
    }*/

   /* public List<Line> getActiveLines(){ // //получить лист активных строк в системе
        List<Line> ret=new ArrayList<>();
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())ret.add(lines.get(i));
        return ret;
    }*/
}
