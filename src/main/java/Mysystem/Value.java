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
public class Value extends Activable{
    private int value;
    private Variable variable;

    public boolean[] boolVector;

    @Override
    void onActivate(){
    }

    public void setVariable(Variable var) {
        this.variable = var;
    }

    public int getValue() {
        return value;
    }

    public Variable getVar() {
        return variable;
    }
    public Value(int cv){
        value=cv;
    }
}
