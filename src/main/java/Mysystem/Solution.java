package Mysystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {
    List<SolvedVariable> solution;
    List<String> decisions;
    HashMap<SolvedVariable,SolvedVariable>  uniqueSolution;

    public List<SolvedVariable> getSolution() {
        return solution;
    }

    public List<String> getDecisions() {
        return decisions;
    }
    
    public Solution(List<Variable> vars, List<Decision> dec){
        solution = new ArrayList<>();
        decisions = new ArrayList<>();
        for (Variable var : vars)
            solution.add(new SolvedVariable(var));
        for (Decision decision : dec)
            decisions.add(decision.getDescr());
    }
    public Solution(List<SolvedVariable> variables){
    solution=variables;
    }
    
    public String solutiontoString(boolean withcodes){
        String ret="";
        for (SolvedVariable solvedVariable : solution)
            ret += solvedVariable.toString(withcodes);
        return ret;
    }

    public String decisionstoString(){
        String ret="";
        for (String decision : decisions)
            ret += decision + "\n";
        return ret;
    }
}
