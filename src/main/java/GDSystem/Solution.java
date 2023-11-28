package GDSystem;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    HashMap<Variable,Integer> placedVariables;

    public String toString(){
        String res="\nSolution:\n";
        for(Map.Entry<Variable,Integer> map:placedVariables.entrySet())
            res+="variable "+map.getKey()+": "+map.getValue()+"\n";
        return res;
    }
    public Solution(){
        placedVariables=new HashMap<>();
    }
}
