/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import com.beust.jcommander.internal.Maps;
import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class SolvedVariable {
    Mysystem.Myvariable variable;
    List<Mysystem.CodedValue> values;
    
    public SolvedVariable(Mysystem.Myvariable vr){
        values = new ArrayList<>();
        variable = vr;
        List<Myvalue> dom=vr.getDomain();
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
