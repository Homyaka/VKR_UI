package GDSystem.UnarConstaints;

import GDSystem.Grid;
import GDSystem.Variable;

public class CornerConstraint extends UnarConstraints{

    public Variable variable;
    public Grid grid;

    @Override
    public void propagate(){

    }
    public CornerConstraint(Variable v,Grid grid){
        super("corner");
        variable=v;
        this.grid=grid;
    }
}
