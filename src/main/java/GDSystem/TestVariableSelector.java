package GDSystem;

import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

public class TestVariableSelector implements VariableSelector<IntVar> {
    @Override
    public IntVar getVariable(IntVar[] intVars) {
        for(IntVar intVar:intVars) if(!intVar.isInstantiated()) {
            System.out.print("select variable: "+intVar.toString()+'\n');
            return intVar;
        }
        System.out.print("NO SELECT VARIABLE :(");
        return null;
    }
}
