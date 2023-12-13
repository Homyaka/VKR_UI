package GDSystem;

import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    HashMap<Variable,Integer> placedVariables;
    public List<Variable> variables;
    public List<Integer> values;

    public Solution(List<Variable> variables){
        placedVariables=new HashMap<>();
        values=new ArrayList<>();
        this.variables=variables;
        for(int i=0;i<variables.size();i++) {
            values.add(variables.get(i).intVar.getValue());
            placedVariables.put(variables.get(i), variables.get(i).intVar.getValue());
        }
    }
}
