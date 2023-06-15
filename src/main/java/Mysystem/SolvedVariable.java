/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;


public class SolvedVariable {
    Variable variable;
    List<CodedValue> values;
    
    public SolvedVariable(Variable vr){
        values = new ArrayList<>();
        variable = vr;
        List<Value> dom=vr.getDomain();
        for(int i=0; i<dom.size();i++)
            if(dom.get(i).isActive())
                values.add(dom.get(i).getValue());
    }
    
    public String toString(boolean withcodes){
            String ret="";
            ret=ret+variable.getName()+"={";
            for(int i=0; i<values.size(); i++){
                ret+=values.get(i).getValue();
                if(withcodes) ret+="("+values.get(i).getCode()+")";
                if(i!=values.size()-1)ret+=",";
            }
            ret+="}\n";
            return ret;
    }

}
