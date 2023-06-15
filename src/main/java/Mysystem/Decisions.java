/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
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
        //System.out.println(this.toString()+"\n"+sys.toString());
        

    }
    
    public Decision getDec(int n){
        return decisions.get(n);
    }
    public void deleteDec(){
       decisions.remove(decisions.size()-1);
    }
    public void deleteDec(int n){
       decisions.remove(n);
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

    public void unapplyto(Decision dc, boolean act){
        int i=decisions.size()-1;
        Decision tmp;
        do{
            tmp =decisions.get(i); 
            tmp.unapply(act);
            decisions.remove(i);
            i--;
        }while(tmp!=dc);
    }

    
    
}
