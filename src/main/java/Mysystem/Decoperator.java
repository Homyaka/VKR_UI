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

/**
 *
 * @author User
 */
public class Decoperator implements DecisionOperator<IntVar>{
    private final Decisions decisions;
    public Decoperator(Decisions decs){
        decisions=decs;
    }
    
    @Override
    public MyDecoperatorop opposite(){
        return null;
    }

    @Override
    public void apply(IntVar v, int i, ICause icause) throws ContradictionException {
        System.out.println("Apply"+v.toString());
        Node nd=(Node)decisions.getDec(i).getChanges().get(0);
        System.out.println("Apply!!!"+nd.getColumn().getVariable().toString());
        int []upd=nd.toIntarr();
        IntIterableBitSet q = new IntIterableBitSet ();
        q.addAll(upd);
        v.removeAllValuesBut(q, icause);
        Decision newmy = new Decision(nd.getComplvals(), "Values "+nd.toString()+" is chosen for domain of variable "+nd.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);
        System.out.println("1Apply"+v.toString());
        System.out.println("2Apply!!!"+nd.getColumn().getVariable().toString());
        System.out.println("3Apply:"+nd.toString()+"\n"+nd.getLine().getSystem().toString());

    }

    @Override
    public void unapply(IntVar v, int i, ICause icause) throws ContradictionException {
        Node node=(Node)decisions.getDec(i).getChanges().get(0);
        System.out.println("Before Unapply:"+node.toString()+"\n"+node.getLine().getSystem().toString());

        decisions.unapplyto(i, false);
        System.out.println("After Unapply:"+node.toString()+"\n"+node.getLine().getSystem().toString());

        //Node node=(Node)decisions.getDec(i).getChanges().get(0);
        //System.out.println("Unapply:\n"+node.getLine().getSystem().toString());
        Node node1=node.getLine().getDiffNode(node);
        int []upd=node1.toIntarr();
        IntIterableBitSet q = new IntIterableBitSet ();
        q.addAll(upd);
        v.removeAllValuesBut(q, icause);
        Decision newmy = new Decision(node1.getComplvals(), "Values "+node1.toString()+" is chosen for domain of variable "+node1.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);
        System.out.println("new Unapply:"+node1.toString()+"\n"+node.getLine().getSystem().toString());
       /* int []upd=node.toIntarr();
        IntIterableBitSet q = new IntIterableBitSet ();
        q.addAll(upd);
        v.removeValues(q, icause);
        Decision newmy = new Decision(node.getActivevals(), "values "+node.toString()+" is removed from domain of variable "+node.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);*/
    }
    
}
