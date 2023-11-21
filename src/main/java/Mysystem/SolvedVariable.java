/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;


public class SolvedVariable {
    private Variable variable;
    private List<Integer> values;
    
    public SolvedVariable(Variable vr){
        values = new ArrayList<>();
        variable = vr;
        List<Value> dom=vr.getDomain();
        for (Value value : dom)
            if (value.isActive())
                values.add(value.getValue());
    }

    public SolvedVariable(List<Integer> vl,Variable vr){
        values=vl;
        variable=vr;
    }
    
    public String toString(boolean withcodes){
            String ret="";
            ret=ret+variable.getName()+"={";
            for(int i=0; i<values.size(); i++){
                ret+=values.get(i);
                if(withcodes) ret+="("+values.get(i)+")";
                if(i!=values.size()-1)ret+=",";
            }
            ret+="}";
            return ret;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
