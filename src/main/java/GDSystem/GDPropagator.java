package GDSystem;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;
import java.util.List;

public class GDPropagator extends Propagator<IntVar> {

    public Solver solver;
    public Variable arrangedVariable;
    public int point;

    public void reComputeDomains(){
        for(Variable v:solver.unarrangedVariables){
            reComputeDomain(v);
        }
    }
    public void reComputeDomain(Variable v){
        delValuesArrangedVariable(v);
        delTopCells(v);
        delLeftCells(v);
        delTopLeftCells(v);
    }
    public void delValuesArrangedVariable(Variable v){
        for(int i=0;i<arrangedVariable.box.getHeight();i++){
            for(int j=0;j<arrangedVariable.box.getWidth();j++){
                int p=point+i*solver.grid.width+j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
    }

    public void delTopCells(Variable v){
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=0;i<arrangedVariable.box.getWidth();i++){
                int p=point+i-solver.grid.width*j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
    }

    public void delLeftCells(Variable v){
        for(int j=0;j<arrangedVariable.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i+solver.grid.width*j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
    }

    public void delTopLeftCells(Variable v){
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i-solver.grid.width*j;
                int index=v.domain.indexOf(p);
                if (index!=-1) v.domain.remove(index);
            }
        }
    }

   public  GDPropagator(Solver solver){
        this.solver=solver;
    }

    @Override
    public void propagate(int i) throws ContradictionException {

    }

    @Override
    public ESat isEntailed() {
        return null;
    }
}
