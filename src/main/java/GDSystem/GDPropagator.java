package GDSystem;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableBitSet;

import java.util.ArrayList;

public class GDPropagator extends Propagator<IntVar> {

    public MySolver mySolver;
    public IntVar placedVar;
    public Variable arrangedVariable;
    public int point;
    public int index;
    public ArrayList<Integer> listDelPoints;
    public int[] arrDelPoints;

    public void reComputeDomains() throws ContradictionException {
       // System.out.print("ZAHEL V reComputeDomainS");
        for(int i=0;i<vars.length;i++){
            if(i!=index) reComputeDomain(mySolver.variables.get(i));
        }
    }
    public void reComputeDomain(Variable v) throws ContradictionException {
        //System.out.print("ZAHEL V reComputeDomain");
        delValuesArrangedVariable(v);
        delTopCells(v);
        delLeftCells(v);
        delTopLeftCells(v);
        if(v.domain.size()!=0) {
            IntIterableBitSet set = new IntIterableBitSet();
            set.addAll(v.convertDomainFromListToArray());
            v.intVar.removeAllValuesBut(set, this );
        }
    }
    public void delValuesArrangedVariable(Variable v) throws ContradictionException {
        for(int i=0;i<arrangedVariable.box.getHeight();i++){
            for(int j=0;j<arrangedVariable.box.getWidth();j++){
                int p=point+i* mySolver.grid.width+j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
        if(v.domain.size()==0) this.fails();
    }

    public void delTopCells(Variable v) throws ContradictionException {
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=0;i<arrangedVariable.box.getWidth();i++){
                int p=point+i- mySolver.grid.width*j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
        if(v.domain.size()==0) this.fails();
    }

    public void delLeftCells(Variable v) throws ContradictionException {
        for(int j=0;j<arrangedVariable.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i+ mySolver.grid.width*j;
                int index=v.domain.indexOf(p);
                if(index!=-1) v.domain.remove(index);
            }
        }
        if(v.domain.size()==0) {
           // System.out.println("HYUNYA KAKAYA-TO");
            this.fails();
        }
    }

    public void delTopLeftCells(Variable v) throws ContradictionException {
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i- mySolver.grid.width*j;
                int index=v.domain.indexOf(p);
                if (index!=-1) v.domain.remove(index);
            }
        }
        if(v.domain.size()==0) {
            //System.out.print("ttttttttt");
            this.fails();
        }
    }

    public  GDPropagator(IntVar[] intVars, MySolver mySolver){
        super(intVars, PropagatorPriority.BINARY,true);
        this.mySolver = mySolver;
    }

    @Override
    public void propagate(int i) throws ContradictionException {
        //System.out.println("NE KRUTOI PROP\n");
        for(IntVar intVar:vars){
           // System.out.println(intVar+" "+System.identityHashCode(intVar));
        }
    }
    @Override
    public void propagate(int idxVarInProp, int mask) throws ContradictionException{
       // System.out.print("OCHEN` KRUTOI PROP\n");
        //System.out.print(System.identityHashCode(vars[idxVarInProp]));
        index=idxVarInProp;
        placedVar=vars[index];
        point=vars[idxVarInProp].getValue();
        arrangedVariable= mySolver.variables.get(index);
        reComputeDomains();
    }

    @Override
    public ESat isEntailed() {
       // System.out.print("ZAHEL V IsEntailed\n");
        for(IntVar intVar:vars){
            if(!intVar.isInstantiated()) {
                //System.out.print("POKA NE NAHOL\n");
                return ESat.UNDEFINED;
            }
        }
       // System.out.print("FIGNYA");
        return ESat.TRUE;
    }
}
