/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

//import constraintchoco.intvars.*;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class DValueSelector implements IntValueSelector{
    private Decisions decisions;

    public DValueSelector(Decisions decs) {
        decisions=decs;
    }

@Override
    public int selectValue(IntVar var){
        return decisions.size()-1;
    }
}
