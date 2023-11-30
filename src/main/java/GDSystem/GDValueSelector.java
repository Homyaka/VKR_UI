package GDSystem;

import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

public class GDValueSelector implements IntValueSelector {
    public MySolver mySolver;

    @Override
    public int selectValue(IntVar intVar) {
        //System.out.print("SELECT VALUE: "+intVar.toString()+" "+intVar.getValue());
        return intVar.getValue();
    }
}
