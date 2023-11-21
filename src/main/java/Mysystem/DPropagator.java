package Mysystem;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class DPropagator extends Propagator<IntVar>{
    private final DSystem problem;
    private final Decisions decisions;
    public DPropagator(DSystem prblm) {
        super(prblm.getIntVars());
        problem=prblm;
        decisions =prblm.getDecisions();
    }
    private void delRow(Line line){
            List <Activable> lst = new ArrayList<>();
            lst.add(line);
            Decision decision = new Decision(lst, "deleting line "+line.toString(true)+" in system "+problem.getName(), 1);
            decision.apply(false);
            decisions.addDec(decision);
           }
    private boolean checkEmptyRow() throws ContradictionException{
        if(problem.getActiveColumnsCount()>0){
            List<Line> activeLines=problem.getActiveLines();
            for (Line line : activeLines) {
                    if (line.IsEmpty()) {
                        this.fails();
                        return false;
                    }
            }
        return true;
        }
        return false;
    }

    private void checkOnlyVar() throws ContradictionException {
        int i=0;
        checkEmptyRow();
        deleteRowWithFullComp();
        checkOnNotSolution(problem.getProblem().getMinSup());
        while(problem.getActiveLinesCount()>0 && i<problem.getLines().size()){
            Line line = problem.getLines().get(i);
            if(line.isActive()){
                int notempty =-1;
                if(calcNodeWeight((line.getNodes().get(0).getActivevals()))<problem.getProblem().getMinSup()) notempty=1;
                if(line.getNodes().get(1).getActive()==0) notempty=0;
                if(problem.getProblem().containAtt!=-1){
                    Value v=problem.getColumns().get(1).getLocaldomain().get(problem.getProblem().containAtt-1);
                    if(!line.getNodes().get(1).getActivevals().contains(v))
                        notempty=0;
                }
                if(problem.getProblem().noContainAtt!=-1){
                    Value v=problem.getColumns().get(1).getLocaldomain().get(problem.getProblem().noContainAtt-1);
                    if((!line.getNodes().get(1).getActivevals().contains(v)) && problem.getColumns().get(1).getActivedomain().contains(v)) notempty=1;
                }
                if (problem.getColumns().get(1).getActivedomain().size()-problem.getActiveLinesCount()==problem.getProblem().patternLength) notempty=1;
                if(problem.getProblem().subPattern!=null)
                    if (checkOnSubPattern(line)) notempty=0;
                if (problem.getProblem().superPattern!=null)
                    if (checkOnSuperPattern(line)) notempty=1;
                if(notempty!=-1){
                    line.intVar.instantiateTo(notempty, this);
                    Node node=line.getNodes().get(notempty);
                    Decision newmy = new Decision(node.getComplvals(), "changing domain of variable "+node.getColumn().getVariable().getName()+" to "+node.toString()+"in system "+problem.getName(), 1);
                    newmy.apply(false);
                    decisions.addDec(newmy);
                    delRow(line);
                    deleteRowWithFullComp();
                    checkOnNotSolution(problem.getProblem().getMinSup());
                    checkEmptyRow();
                }
                else i++;
            }
            else i++;
        }
    }

    private void deleteRowWithFullComp() throws ContradictionException {
        List<Activable> listDel=null;
        List<Line> activeLines = problem.getActiveLines();
        for (Line line : activeLines) {
                List<Node> nodes = line.getNodes();
                int j = 0;
                while (j < nodes.size()) {
                    if (nodes.get(j).isActive() && nodes.get(j).getActive() == nodes.get(j).getColumn().getActivedomaincount()) {
                        if (listDel == null) listDel = new ArrayList<>();
                        listDel.add(line);
                        line.intVar.instantiateTo(j,this);
                        break;
                    }
                    j++;
                }
        }
        if(listDel!=null){
            Decision newmy = new Decision(listDel, "deleting lines with full nodes from system "+problem.getName(), 1);
            newmy.apply(false);
            decisions.addDec(newmy);
        }
    }

    public int calcNodeWeight(List<Value> values){
        int sum=0;
        if(values.isEmpty()) return -1;
        for(Value v: values){
            int ind=v.getValue()-1;
            sum+=problem.getProblem().weights.get(ind);
        }
        return sum;
    }

    private void checkOnNotSolution(int support) throws ContradictionException{
        Column xCol=problem.getColumns().get(0);
        Column yCol=problem.getColumns().get(1);
        if(problem.getProblem().patternLength!=-1){
            if(problem.getColumns().get(1).getActivedomain().size()<problem.getProblem().patternLength){
                this.fails();
                return;
            }
            if((problem.getColumns().get(1).getActivedomain().size()-problem.getActiveLinesCount())>problem.getProblem().patternLength){
                this.fails();
                return;
            }
        }
        if(problem.getActiveColumnsCount()>0){
            if(calcNodeWeight(xCol.getActivedomain())<support){
                this.fails();
                return;
            }
            if (problem.getProblem().containAtt!=-1) {
                Value v = problem.getColumns().get(1).getLocaldomain().get(problem.getProblem().containAtt - 1);
                if (!yCol.getActivedomain().contains(v)) {
                    this.fails();
                    return;
                }
                return;
            }
            if (problem.getProblem().noContainAtt!=-1){
                Value v=problem.getColumns().get(1).getLocaldomain().get(problem.getProblem().noContainAtt-1);
                if (problem.getColumns().get(1).getActivedomain().size()==1 && problem.getColumns().get(1).getActivedomain().contains(v)){
                    this.fails();
                }
            }
            if(problem.getProblem().subPattern!=null){
                List<Value> domainY= problem.getColumns().get(1).getActivedomain();
                ArrayList<Integer> valY=new ArrayList<>();
                for(Value v:domainY)
                    valY.add(v.getValue());
                for(int i:problem.getProblem().subPattern)
                    if (!valY.contains(i)){
                        this.fails();
                        return;
                    }
            }
            if(problem.getProblem().superPattern!=null){
                List<Value> domY=yCol.getActivedomain();
                List<Integer> intDomY=new ArrayList<>();
                List<Integer> additionAL=additionForActiveLines();
                List<Integer> superPattern=problem.getProblem().superPattern;
                for(Value v:domY)
                    intDomY.add(v.getValue());
                for(int valY:intDomY)
                    if(!superPattern.contains(valY))
                        if (!additionAL.contains(valY))
                            this.fails();
            }
        }
    }

    public void fillAddition(){
        for(int i=0;i<problem.getLines().size();i++)
            problem.getProblem().addition.put(problem.getLines().get(i),i+1);
    }

    public ArrayList<Integer> additionForActiveLines(){
        ArrayList<Integer> res=new ArrayList<>();
        for(int i=0;i<problem.getActiveLinesCount();i++){
            res.add(problem.getProblem().addition.get(problem.getActiveLines().get(i)));
        }
        return res;
    }
    public boolean checkOnSubPattern(Line line){
        List<Integer> subPattern=problem.getProblem().subPattern;
            List<Value> valY=line.getNodes().get(1).getActivevals();
            List<Integer> intY=new ArrayList<>();
            for(Value v:valY)
                intY.add(v.getValue());
            for(int val:subPattern)
                if(!intY.contains(val)) return true;
            return false;
    }
    public boolean checkOnSuperPattern(Line line){
        List<Value> missVals=line.getNodes().get(1).getMissVals();
        int missval=missVals.get(0).getValue();
        if (!problem.getProblem().superPattern.contains(missval)) return true;
        return false;
    }
    @Override
    public void propagate(int evtmask) throws ContradictionException {
        fillAddition();
        additionForActiveLines();
        checkOnlyVar();
    }
    private void systemDeactivate(){
        List<Activable> lst=new ArrayList<>();
        lst.add(problem);
        Decision newmy = new Decision(lst, "solved system is deactivated "+problem.getName(), 1);
        newmy.apply(false);
        decisions.addDec(newmy);
    }

    @Override 
    public ESat isEntailed() {
                if(problem.getActiveLinesCount()==0){ systemDeactivate(); return ESat.TRUE;}
                else return ESat.UNDEFINED;
    }
}
