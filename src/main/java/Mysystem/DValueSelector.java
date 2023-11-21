package Mysystem;

import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

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
