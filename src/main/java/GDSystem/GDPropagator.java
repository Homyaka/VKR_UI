package GDSystem;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableBitSet;

import java.util.ArrayList;
import java.util.Arrays;


public class GDPropagator extends Propagator<IntVar> {

    public MySolver mySolver;
    public IntVar placedVar;
    public Variable arrangedVariable;
    public int point;
    public int index;
    public ArrayList<Integer> newDom;
    public int[] removeddomain;

    public void reComputeDomains() throws ContradictionException {
        for(int i=0;i<vars.length;i++){
            if(!vars[i].isInstantiated()) reComputeDomain(mySolver.variables.get(i));
        }
    }
    public void reComputeDomain(Variable v) throws ContradictionException {
        newDom=new ArrayList<>();
        for(int i:v.intVar) newDom.add(i);
        delValuesArrangedVariable(v);
        delTopCells(v);
        delLeftCells(v);
        delTopLeftCells(v);
        v.domain=newDom;
        if(newDom.size()!=0) {
            IntIterableBitSet set = new IntIterableBitSet();
            set.addAll(v.convertDomainFromListToArray());
            v.intVar.removeAllValuesBut(set, this );
        }
        else this.fails();
    }
    public void delValuesArrangedVariable(Variable v) throws ContradictionException {
        for(int i=0;i<arrangedVariable.box.getHeight();i++){
            for(int j=0;j<arrangedVariable.box.getWidth();j++){
                int p=point+i* mySolver.grid.width+j;
                int index=newDom.indexOf(p);
                if(index!=-1) newDom.remove(index);
            }
        }
        if(newDom.size()==0) {
            this.fails();
        }
    }

    public void delTopCells(Variable v) throws ContradictionException {
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=0;i<arrangedVariable.box.getWidth();i++){
                int p=point+i- mySolver.grid.width*j;
                //System.out.println("delTopCells\ni= "+i+",j= "+j+"point="+p);
                int index=newDom.indexOf(p);
                if(index!=-1) newDom.remove(index);
            }
        }
        if(newDom.size()==0) {
            //System.out.println("NEL`ZYA POSTAVIT`");
            this.fails();
        }
    }

    public void delLeftCells(Variable v) throws ContradictionException {
        for(int j=0;j<arrangedVariable.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i+ mySolver.grid.width*j;
                //System.out.println("delLeftCells\ni= "+i+",j= "+j+"point="+p);
                int index=newDom.indexOf(p);
                if(index!=-1) newDom.remove(index);
            }
        }
        if(newDom.size()==0) {
            //System.out.println("NEL`ZYA POSTAVIT`");
            this.fails();
        }
    }

    public void delTopLeftCells(Variable v) throws ContradictionException {
        for (int j=1;j<v.box.getHeight();j++){
            for(int i=1;i<v.box.getWidth();i++){
                int p=point-i- mySolver.grid.width*j;
                //System.out.println("delTopLeftCells\ni= "+i+",j= "+j+"point="+p);
                int index=newDom.indexOf(p);
                if (index!=-1) newDom.remove(index);
            }
        }
        if(newDom.size()==0) {
            //System.out.println("NEL`ZYA POSTAVIT`");
            this.fails();
        }
    }

    public  GDPropagator(IntVar[] intVars, MySolver mySolver){
        super(intVars, PropagatorPriority.BINARY,true);
        this.mySolver = mySolver;
    }

    @Override
    public void propagate(int i) throws ContradictionException {
        //System.out.println("\n ZAHOL V NE KRUTOI PROP\n");
        for(IntVar intVar:vars){
           // System.out.println(intVar+" "+System.identityHashCode(intVar));
        }
    }
    @Override
    public void propagate(int idxVarInProp, int mask) throws ContradictionException{
        //System.out.println("\nДо распространения: "+idxVarInProp);
        for(IntVar v:vars){
            //System.out.print(v);
            //if(v.isInstantiated()) System.out.print(" Поставлена\n");
            //else System.out.println("\n");
        }
        index=idxVarInProp;
        placedVar=vars[index];
        point=vars[idxVarInProp].getValue();
        arrangedVariable= mySolver.variables.get(index);
       // System.out.print("Поставлена: Variable"+arrangedVariable.variableToString());
        if(vars[idxVarInProp].isInstantiated()){
            reComputeDomains();
            //System.out.println("\nПосле распространения:");
            for(IntVar v:vars){
            //System.out.println(v);
            }
        }
       // else System.out.println("БЕЗ РАСПРОСТРАНЕНИЯ");
    }

    @Override
    public ESat isEntailed() {
       // System.out.print("ZAHEL V IsEntailed: ");
        for(IntVar intVar:vars){
            if(!intVar.isInstantiated()) {
               // System.out.print("POKA NE NAHOL\n");
                return ESat.UNDEFINED;
            }
        }
       // System.out.print("NAHOL\n");
        return ESat.TRUE;
    }
}
