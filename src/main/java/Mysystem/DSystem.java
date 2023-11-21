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

    public int getActiveLinesCount() {
        int count=0;
        for(int i=0; i<lines.size(); i++)
            if(lines.get(i).isActive())count++;
        return count;
    }

    public int getActiveColumnsCount() {
        int count=0;
        for(int i=0; i<columns.size(); i++)
            if(columns.get(i).isActive())count++;
        return count;
    }

    public void addColumn(Column column){
        columns.add(column);
        column.setSystem(this);
    }

    public void addLine(Line lin){
        lines.add(lin);
        lin.setSystem(this);
    }

    public List<Line> getActiveLines(){
        List<Line> activeLines=new ArrayList<>();
        for(Line line: getLines()){
            if(line.isActive()) activeLines.add(line);
        }
        return activeLines;
    }

    @Override
    public String toString(){
        String ret="";
        for (Column column : columns) ret = ret + column.toString() + " ";
        ret=ret+"\n";
        for (Line line : lines)
            if (line.isActive())
                ret = ret + line.toString() + "\n";
        return ret;
    }
    public IntVar[] getIntVars(){
        List<IntVar> ret = new ArrayList<>();
        for (Line line : lines) ret.add(line.intVar);
        return ret.toArray(new IntVar[0]);
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
}
