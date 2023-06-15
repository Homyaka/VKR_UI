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
public class MyDecoperatorop implements DecisionOperator<IntVar>{
    private Decisions decisions;
    public MyDecoperatorop(Decisions decs){
        decisions=decs;
    }
    
    @Override
    public Decoperator opposite(){
        System.out.println("2opposite");
        return new Decoperator(decisions);
    }

    @Override
    public void apply(IntVar v, int i, ICause icause) throws ContradictionException {
        System.out.println("2apply");
        Node nd=(Node)decisions.getDec(i).getChanges().get(0);
        System.out.println(nd.getColumn().getSystem().toString());
        int []upd=nd.toIntarr();
        IntIterableBitSet q = new IntIterableBitSet ();
        q.addAll(upd);
        v.removeValues(q, icause);
        System.out.println(v.toString());
        Decision newmy = new Decision(nd.getActivevals(), "values "+nd.toString()+" is removed from domain of variable "+nd.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);

    }

    @Override
    public void unapply(IntVar v, int i, ICause icause) throws ContradictionException {
        System.out.println("2unapply");
        Node nd=(Node)decisions.getDec(i).getChanges().get(0);
        int []upd=nd.toIntarr();
        IntIterableBitSet q = new IntIterableBitSet ();
        q.addAll(upd);
        v.removeAllValuesBut(q, icause);
        System.out.println(v.toString());
        Decision newmy = new Decision(nd.getComplvals(), "Values "+nd.toString()+" is chosen for domain of variable "+nd.getColumn().getVariable().getName(), 3);
        newmy.apply(false);
        decisions.addDec(newmy);
    }
    
}
