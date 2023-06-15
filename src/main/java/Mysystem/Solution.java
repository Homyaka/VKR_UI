/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    List<SolvedVariable> solution;
    List<String> decisions;

    public List<SolvedVariable> getSolution() {
        return solution;
    }

    public List<String> getDecisions() {
        return decisions;
    }
    
    public Solution(List<Variable> vars, List<Decision> dec){
        solution = new ArrayList<>();
        decisions = new ArrayList<>();
        for(int i=0; i<vars.size(); i++)
            solution.add(new SolvedVariable(vars.get(i)));
        for(int i=0; i<dec.size(); i++)
            decisions.add(dec.get(i).getDescr());
    }
    
    public String solutiontoString(boolean withcodes){
        String ret="";
        for(int i=0; i<solution.size();i++)
            ret+=solution.get(i).toString(withcodes);
        return ret;
    }

    public String decisionstoString(){
        String ret="";
        for(int i=0; i<decisions.size();i++)
            ret+=decisions.get(i)+"\n";
        return ret;
    }
    
}
