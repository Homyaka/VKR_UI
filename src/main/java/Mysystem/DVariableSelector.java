package Mysystem;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

public class DVariableSelector implements VariableSelector<IntVar>{
    private final Problem problem;
    public DVariableSelector(Problem my){
        problem=my;
    }

    @Override
    public IntVar getVariable(IntVar[] vars){
        for(IntVar i: vars){
            if (!i.isInstantiated()) {
                List<Activable> lst = new ArrayList<>();
                Node node=problem.mapIntVarLine.get(i).getNodes().get(0);
                lst.add(node);
                Decision dec = new Decision(lst, "Variable "+node.getColumn().toString()+" is chosen",2);
                problem.getDecisions().addDec(dec);
                return i;
            }
        }
        return null;
    }
}
