package GDSystem;

import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import java.util.Set;

public class GDVariableSelector implements VariableSelector<IntVar> {

    @Override
    public IntVar getVariable(IntVar[] intVars) {
        for(IntVar intVar:intVars){
            if (!intVar.isInstantiated()) {
              //  System.out.println("SELECT VARIABLE:"+intVar.toString()+" "+System.identityHashCode(intVar));
                return intVar;
            }
        }
      //  System.out.println("OPYAT` FIGNYA");
        return null;
    }
}
