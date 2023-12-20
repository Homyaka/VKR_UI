package GDSystem.UnarConstaints;

import GDSystem.Direction;
import GDSystem.Grid;
import GDSystem.Variable;

public class DistanceToWallConstraint extends UnarConstraints {
    Direction direction;
    int distance;
    Variable variable;
    Grid grid;

    @Override
    public void propagate(){

    }
}
