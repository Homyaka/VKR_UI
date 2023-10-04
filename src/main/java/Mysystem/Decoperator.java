/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import org.chocosolver.solver.ICause;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableBitSet;


public class Decoperator implements DecisionOperator<IntVar>{
    private final Decisions decisions;
    public Decoperator(Decisions decs){
        decisions=decs;
    }
    @Override
    public void apply(IntVar v, int i, ICause icause) throws ContradictionException {
        Node nd=(Node)decisions.getDec(i).getChanges().get(0);
        v.instantiateTo(0,icause);
        System.out.println("ВЕТВЛЕНИЕ ПО Х");
        Decision newmy = new Decision(nd.getComplvals(), "Values "+nd.toString()+" is chosen for domain of variable "+nd.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);
    }
    @Override
    public void unapply(IntVar v, int i, ICause icause) throws ContradictionException {
        decisions.unapplyto(i, false);
        v.instantiateTo(1,icause);
        System.out.println("ВЕТВЛЕНИЕ ПО У");
        Node node1=((Node)decisions.getDec(i).getChanges().get(0)).getLine().getNodes().get(1);
        Decision newmy = new Decision(node1.getComplvals(), "Values "+node1.toString()+" is chosen for domain of variable "+node1.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);
    }

    @Override
    public DecisionOperator<IntVar> opposite() {
        return null;
    }

}
