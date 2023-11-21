package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Column extends Activable{
    
    private final List<Value> localdomain;
    private final List<Node> nodes;
    private Variable variable;
    private DSystem system;
    
    @Override
    void onActivate(){
        int stat;
        if (this.getDec()==null) stat=0;
        else stat = this.getDec().getStatus();
        for(int i = 0; i<nodes.size(); i++)
            if(nodes.get(i).getLine().isActive()) nodes.get(i).activate(this.isActive(), this.getDec(),stat);
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
    public Variable getVariable() {
        return variable;
    }
    public DSystem getSystem() {
        return system;
    }
    public void setSystem(DSystem system) {
        this.system = system;
    }
    public int getActivedomaincount() {
        int count=0;
        for(int i=0; i<localdomain.size(); i++)
            if(localdomain.get(i).isActive())count++;
        return count;
    }

    public int getActivenodescount() {
        int count=0;
        for(int i=0; i<nodes.size(); i++)
            if(nodes.get(i).isActive())count++;
        return count;
    }
    public List<Value> getLocaldomain() {
        return localdomain;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Column(){
        localdomain=new ArrayList<>();
        nodes=new ArrayList<>();
    }

    public Column(List<Value> dom){
        nodes=new ArrayList<>();
        localdomain = dom;
    }

    public void addValue(Value val){
        localdomain.add(val);
    }
    
    public void addNode(Node add){
        nodes.add(add);
        add.setColumn(this);
    }

    @Override
    public String toString(){
        String ret=variable.getName()+":"+ "{";
        for(int i =0; i<localdomain.size(); i++)
            if(localdomain.get(i).isActive())ret+=localdomain.get(i).getValue()+";"/*+"(nodes: "+domain.get(i).getNodes().size()+");"*/;
    ret+="} "/*+chocovar.toString()*/;
    return ret;
        
    }
    
    public Value getValue(int code){
        for(int i=0; i<localdomain.size(); i++)
            if(localdomain.get(i).getValue()==code) return localdomain.get(i);
        return null;
    }

    public List<Value> getActivedomain(){
        List<Value> ret = new ArrayList<>();
        for(int i=0; i<localdomain.size(); i++){
            Value val=localdomain.get(i);
            if(val.isActive()) ret.add(val);}
        return ret;
    }
}
