/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

//import constraintchoco.intvars.*;
import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableBitSet;

public class DPropagator extends Propagator<IntVar>{
    private final DSystem problem;
    private final Decisions decisions;
    private  int minSup=2;
    private void delRow(Line line){
            List <Activable> lst = new ArrayList<>();
            lst.add(line);
            Decision decision = new Decision(lst, "deleting line "+/*lin.toString(true)+*/" in system "+problem.getName(), 1);
            decision.apply(false);
            decisions.addDec(decision);
    }
    private boolean checkEmptyRow() throws ContradictionException{ //1 утверждение
        if(problem.getActivecolumnscount()>0){
            List<Line> lines = problem.getLines();
            for (Line line : lines) {
                if (line.isActive()) {
                    if (line.IsEmpty()) {
                        this.fails();
                        return false;
                    }
                }
            }
        return true;
        }
        return false;
    }
    private void deleteEmptyCol(){ //2 утверждение
        List<Activable> list=null;
        List<Column> columns = problem.getColumns();
        for (Column column : columns) {
            if (column.isActive()) {
                List<Node> nodes = column.getNodes();
                boolean flagStop = false;
                int j = 0;
                while (!flagStop && j < nodes.size()) {
                    Node tempnode = nodes.get(j);
                    if (tempnode.isActive() && tempnode.getActive() != 0) flagStop = true;
                    j++;
                }
                if (!flagStop) {
                    if (list == null) list = new ArrayList<>();
                    list.add(column);
                }
            }
        }
        if(list!=null){
            Decision decision = new Decision(list, "deleting empty columns in system "/*+problem.getName()*/, 1);
            decision.apply(false);
            decisions.addDec(decision);
        }
    }
    private void checkOnlyVar() throws ContradictionException { // 3 утверждение
        int i=0;
        checkDomainOnLessSup(minSup);
       // changeCompOnEmptyComp(minSup);
        checkEmptyRow(); // 1 statement
        deleteRowWithFullComp(); // 4 statement
        deleteEmptyCol(); // 2 statement
        while(problem.getActivelinescount()>0 && i<problem.getLines().size()){
            Line line = problem.getLines().get(i);
            if(line.isActive()){
                int j=0;
                Node notempty =null;
                if(line.getNodes().get(0).getActive()<minSup) notempty=line.getNodes().get(1);
                if(line.getNodes().get(1).getActive()==0) notempty=line.getNodes().get(0);
                if(notempty!=null){
                    int [] qq = notempty.toIntarr();
                    if(qq.length>1) {
                        IntIterableBitSet q = new IntIterableBitSet ();
                        q.addAll(qq);
                        notempty.getColumn().getVariable().getChocovar().removeAllValuesBut(q, this);
                    }
                    else notempty.getColumn().getVariable().getChocovar().instantiateTo(qq[0], this);
                    Decision newmy = new Decision(notempty.getComplvals(), "changing domain of variable "+notempty.getColumn().getVariable().getName()+" to "+notempty.toString()+"in system "+problem.getName(), 1);
                    newmy.apply(false);
                    decisions.addDec(newmy);
                    delRow(line);
                    deleteRowWithFullComp();
                    checkDomainOnLessSup(2);
                   // changeCompOnEmptyComp(2);
                    if(checkEmptyRow()){
                        deleteEmptyCol();
                        i=1;}
            }
            else i++;
        }else i++;
        }
    }
    private void deleteRowWithFullComp(){  //4 утверждение
        List<Activable> list=null;
        List<Line> lines = problem.getLines();
        for (Line line : lines) {
            if (line.isActive()) {
                List<Node> nodes = line.getNodes();
                int j = 0;
                while (j < nodes.size()) {
                    if (nodes.get(j).isActive() && nodes.get(j).getActive() == nodes.get(j).getColumn().getActivedomaincount()) {
                        if (list == null) list = new ArrayList<>();
                        list.add(line);
                        break;
                    }
                    j++;
                }
            }
        }
        if(list!=null){
            Decision newmy = new Decision(list, "deleting lines with full nodes from system "+problem.getName(), 1);
            newmy.apply(false);
            decisions.addDec(newmy);
        }
    }
    private boolean checkDomainOnLessSup(int support) throws ContradictionException{
        if(problem.getActivecolumnscount()>0){
            Column column=problem.getColumns().get(0);
                   if( column.getActivedomaincount() <support) {
                       this.fails();
                       return false;
                   }
                return true;
                }
        return false;
    }

   /* private void changeCompOnEmptyComp(int support){
        List<Node> listChange = null;
        List<Line> listLines=problem.getLines();
        for (Line line: listLines) {
            if (line.isActive()){
                List<Node> nodes= line.getNodes();
                for (Node node:nodes) {
                    if(node){
                        if (node.getValues().size()<support) {
                            if (listChange==null)
                            {
                                listChange = new ArrayList<>();
                                listChange.add(node);
                            }
                        }
                    }
                }
            }
        }
        if (listChange!=null){
            for (Node node:listChange){
                List<Activable> list=node.getActivevals();
                Decision dec= new Decision(list,"delete all values from conp less min sup"+problem.getName(),1);
                dec.apply(false);
                decisions.addDec(dec);
            }
        }
    }*/
    
    public DPropagator(DSystem prblm) {
        super(prblm.getIntvars());
        problem=prblm;
        decisions =prblm.getDecisions();
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        System.out.println("Before prop:"+problem.toString());
            checkOnlyVar();
        System.out.println("after prop:"+problem.toString());
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
                if(problem.getActivelinescount()==0){ systemDeactivate(); return ESat.TRUE;}
                else return ESat.UNDEFINED;

    }
}
