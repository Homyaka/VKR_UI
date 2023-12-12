package GDSystem;

import org.chocosolver.solver.variables.IntVar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    HashMap<Variable,Integer> placedVariables;
    public List<Variable> variables;

    public Solution(List<Variable> variables){
        placedVariables=new HashMap<>();
        this.variables=variables;
        for(int i=0;i<variables.size();i++)
            placedVariables.put(variables.get(i),variables.get(i).intVar.getValue());

    }
}
