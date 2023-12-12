package GDSystem;

import org.chocosolver.solver.variables.IntVar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    HashMap<Variable,Integer> placedVariables;
    public List<Variable> variables;
    public IntVar[] intVars;

    public Solution(List<Variable> variables,IntVar[] intVars){
        placedVariables=new HashMap<>();
        this.variables=variables;
        this.intVars=intVars;
        for(int i=0;i<variables.size();i++)
            placedVariables.put(variables.get(i),intVars[i].getValue());

    }
}
