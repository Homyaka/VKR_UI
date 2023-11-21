package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Decisions {
    private List<Decision> decisions;

    public List<Decision> getDecisions() {
        return decisions;
    }

    public Decisions(){
        decisions = new ArrayList<>();
    }
    
    public void addDec(Decision dec){
        decisions.add(dec);
    }
    
    public Decision getDec(int n){
        return decisions.get(n);
    }
    
    public int size(){
        return decisions.size();
    }
    
    @Override
    public String toString(){
        String ret="";
        for(int i=0; i<decisions.size(); i++)
            ret+=decisions.get(i).getDescr()+"\n";
        return ret;
    }
    
    public void unapplyto(int n, boolean act){
        int i=decisions.size()-1;
        while(i>n){
            decisions.get(i).unapply(act);
            decisions.remove(i);
            i--;
        }
    }
}
