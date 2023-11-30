package GDSystem;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableBitSet;

import java.util.ArrayList;

public class TestProp extends Propagator<IntVar> {

    public TestProp(IntVar[] testIntvars){
        super(testIntvars, PropagatorPriority.LINEAR,true);
    }
    @Override
    public void propagate(int i) throws ContradictionException {
        System.out.print("ZAHEL V NEKRUTOI PROP\n");
       /* ArrayList<Integer> rem=new ArrayList<>();
        for(int v:vars[0]) if (v<=vars[1].getValue()) rem.add(v);
        int[] q=new int[rem.size()];
        for(int j=0;j<rem.size();j++) q[j]=rem.get(j);
        IntIterableBitSet set=new IntIterableBitSet();
        set.addAll(q);
        vars[0].removeValues(set,this);*/
    }
    @Override
    public void propagate(int idxVarInProp, int mask) throws ContradictionException {
        System.out.print("ZAHEL V KRUTOI PROP\n");
        ArrayList<Integer> rem=new ArrayList<>();
        ArrayList<Integer> rem0=new ArrayList<>();
        System.out.print(vars[0].getUB()+"\n");
        for(int w:vars[0]) if (w<=vars[1].getLB()) rem0.add(w);
        for(int v:vars[1]) if (v>=vars[0].getUB()) rem.add(v);
        int[] q=new int[rem.size()];
        int[] q1= new int[rem0.size()];
        for(int j=0;j<rem.size();j++) q[j]=rem.get(j);
        for(int j=0;j<rem0.size();j++) q1[j]=rem0.get(j);
        IntIterableBitSet set=new IntIterableBitSet();
        IntIterableBitSet set0=new IntIterableBitSet();
        set.addAll(q);
        set0.addAll(q1);
        vars[0].removeValues(set0,this);
        vars[1].removeValues(set,this);
    }

    @Override
    public ESat isEntailed() {
        if(vars[0].getDomainSize()==0||vars[1].getDomainSize()==0){
            System.out.print("FIGNYA\n");
            return ESat.FALSE;
        }
        if(!vars[1].isInstantiated()||!vars[0].isInstantiated()) {
            System.out.print("POKA NE RESHENO: "+vars[0]+" "+vars[1]+'\n');
            return ESat.UNDEFINED;
        }
        if(vars[0].isInstantiated()&&vars[1].isInstantiated()) {
            System.out.print("VRODE ZBC:"+vars[0]+" "+vars[1]+'\n');
            return ESat.TRUE;
        }
        System.out.print("OPYAT FIGNYA\n");
        return ESat.FALSE;
    }
}
