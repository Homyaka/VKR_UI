package Mysystem;

import org.chocosolver.solver.variables.IntVar;
import java.util.ArrayList;
import java.util.List;


public class Line extends Activable {
    private DSystem system;
    private List<Node> nodes;
    public IntVar intVar;
    
    @Override
    void onActivate(){
        int stat;
        if (this.getDec()==null)stat=0;
        else stat = this.getDec().getStatus();
        for(int i = 0; i<nodes.size(); i++)
            if(nodes.get(i).getColumn().isActive())nodes.get(i).activate(this.isActive(), this.getDec(), stat);
    }

    public void setSystem(DSystem system) {
        this.system = system;
    }

    public DSystem getSystem() {
        return system;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getActive() {
        int count=0;
        for(int i=0; i<nodes.size(); i++)
            if(nodes.get(i).isActive())count++;
        return count;
    }
    public Line(){
        nodes=new ArrayList<>();
    }

    public void addNode(Node add){
        nodes.add(add);
        add.setLine(this);
    }
    
    public boolean IsEmpty(){
        for (Node node : nodes)
            if (node.isActive() && node.getActive() != 0)
                return false;
        return true;    
    }
    public boolean hasempty(){
        for (Node node : nodes)
            if (node.isActive() && node.getActive() == 0)
                return true;
        return false;    
    }
    @Override
    public String toString(){
        String ret="";
        if(this.isActive()){
            for (Node node : nodes)
                ret += node.toString() + " ";
        }
        return ret;
    }
    public String toString(boolean withinactive){
        String ret="";
        if(this.isActive()){
            for(int i=0; i<nodes.size(); i++){
                ret=ret+nodes.get(i).getColumn().getVariable().getName()+":";
                if(nodes.get(i).isActive())ret+=nodes.get(i).toString()+" ";
                else ret+="{} ";
            }
        }
        return ret;
    }

}
