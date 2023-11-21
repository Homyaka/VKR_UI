package Mysystem;

public class Value extends Activable{
    private int value;

    private Variable variable;

    @Override
    void onActivate(){}
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
