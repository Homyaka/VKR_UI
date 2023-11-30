package GDSystem;

import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

public class TestValueSelector implements IntValueSelector {
    @Override
    public int selectValue(IntVar intVar) {
        System.out.print("Select value: "+intVar.getValue()+'\n');
        return intVar.getValue();
    }
}
