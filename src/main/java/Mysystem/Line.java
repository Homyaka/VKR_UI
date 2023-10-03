/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public int getActive() {//количество активных ячеек
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
    
    public boolean IsEmpty(){ //проверяет пустая ли строка
        for(int i=0; i<nodes.size(); i++)
            if(nodes.get(i).isActive()&&nodes.get(i).getActive()!=0)return false;
        return true;    
    }
    public boolean hasempty(){ //проверяет есть ли пустые ячейки
        for(int i=0; i<nodes.size(); i++)
            if(nodes.get(i).isActive()&&nodes.get(i).getActive()==0)return true;
        return false;    
    }
    @Override
    public String toString(){ //отладочный
        String ret="";
        if(this.isActive()){
            for(int i=0; i<nodes.size(); i++)
                ret=ret+nodes.get(i).toString()+" ";
        }
        //return ret+"active: "+active;
        return ret;
    }
    public String toString(boolean withinactive){ //отладочный
        String ret="";
        if(this.isActive()){
            for(int i=0; i<nodes.size(); i++){
                ret=ret+nodes.get(i).getColumn().getVariable().getName()+":";
                if(nodes.get(i).isActive())ret+=nodes.get(i).toString()+" ";
                else ret+="{} ";
            }
        }
        //return ret+"active: "+active;
        return ret;
    }
    public Node getnodefromcol(Column col){
        Node ret=null;
        for(int i=0; i<nodes.size(); i++)
            if(nodes.get(i).getColumn()==col)return nodes.get(i);
        return ret;
    }

}
