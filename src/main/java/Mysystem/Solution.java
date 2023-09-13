/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        for(int i=0; i<vars.size(); i++)
            solution.add(new SolvedVariable(vars.get(i)));
        for(int i=0; i<dec.size(); i++)
            decisions.add(dec.get(i).getDescr());
    }
    public Solution(List<SolvedVariable> variables){

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

    public Solution stringToSolution(String stringSolution){
        String[] line=stringSolution.split("\n");
        String x=line[0];
        String y=line[1];
        x=x.substring(0,x.length()-1);
        List<Integer> varX=new ArrayList<>();
        List<Integer> varY=new ArrayList<>();
        for(String s:x.split(",")) varX.add(Integer.parseInt(s));
        for (String s:y.split(",")) varY.add(Integer.parseInt(s));
        SolvedVariable svX=new SolvedVariable(varX);
        SolvedVariable svY=new SolvedVariable(varY);
        List<SolvedVariable> sol=new ArrayList<>();
        sol.add(svX);
        sol.add(svY);
        return new Solution(sol);
    }

    
}
