/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

   /* private Myvariable getminVar(){
        //List<Myvariable> vars=problem.getActiveVars();
        //if(vars.isEmpty())return null;
        //Myvariable ret = vars.get(0);
       return problem.getVars().get(0);
    }
    
    private Node getNodeInMinStr(Myvariable var){
        List<Node> nds=var.getColumns().get(0).getActivenodes();
        if(nds.size()==0)return null;
        return nds.get(0);
    }*/

    @Override
    public IntVar getVariable(IntVar[] vars){
        /*System.out.println("varsel1:"+problem.getProblems().get(0).toString());
        Myvariable var=getminVar();
        System.out.println("varsel:"+var.toString());
        System.out.println("varsel2:"+problem.getProblems().get(0).toString());
        Node nd = getNodeInMinStr(var);
        if(nd==null) return null;
        System.out.println("selected:"+nd.toString());
       // IntVar ret=nd.getColumn().getVariable().getChocovar();*/
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
       // System.out.println("intvarsel:"+ret.toString());
        /*List<Activable> lst = new ArrayList<>();
        lst.add(nd);
        Decision dec = new Decision(lst, "Variable "+nd.getColumn().toString()+" is chosen",2);
        problem.getDecisions().addDec(dec);
        return ret;*/
    }
    
}
