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
        for(int i=0; i<dom.size();i++)
            if(dom.get(i).isActive())
                values.add(dom.get(i).getValue());
    }

    public SolvedVariable(List<Integer> vl){
        values=vl;
    }

    public int GetValuesNum(){
        int num=0;
        for(int i:this.values)
            num++;
        return num;
    }
    
    public String toString(boolean withcodes){
            String ret="";
            ret=ret+variable.getName()+"={";
            for(int i=0; i<values.size(); i++){
               /* ret+=values.get(i).getValue();
                if(withcodes) ret+="("+values.get(i).getCode()+")";*/
                ret+=values.get(i);
                if(withcodes) ret+="("+values.get(i)+")";
                if(i!=values.size()-1)ret+=",";
            }
            ret+="}\n";
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
