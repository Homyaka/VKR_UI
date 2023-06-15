/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

/**
 *
 * @author User
 */
public class Myvalue extends Activable{
    private CodedValue value;
    private Myvariable variable;
    
    @Override
    void onActivate(){
    }

    public void setVariable(Myvariable var) {
        this.variable = var;
    }

    public CodedValue getValue() {
        return value;
    }

    public Myvariable getVar() {
        return variable;
    }
    public Myvalue(CodedValue cv){
        value=cv;
    }
}
