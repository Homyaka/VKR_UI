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
public class Valuevocab {
    private List<CodedValue> Vocab;

    public List<CodedValue> getVocab() {
        return Vocab;
    }

    public Valuevocab(){
        Vocab=new ArrayList<>();
    }
    public int getSize() {
        return Vocab.size();
    }
    
    public CodedValue addValue(String val){
        CodedValue ret = new CodedValue(val,Vocab.size());
        int indx = getIndx(val);
        if(indx==-1)
            Vocab.add(ret);
        else
            Vocab.add(indx, ret);
        return ret;
    }
    
    private int getIndx(String val){
        if(Vocab.isEmpty()) return -1;
        int lb=0;
        int ub=Vocab.size()-1;
        while(ub-lb>1){
            int cmp = Vocab.get((ub+lb)/2).getValue().compareTo(val);
            if(cmp<0) lb=(ub+lb)/2;
            
            if(cmp>0) ub=(ub+lb)/2;
            if(cmp==0)return (ub+lb)/2;
        }
        if(Vocab.get(lb).getValue().compareTo(val)>0) return lb;
        else 
            if(Vocab.get(ub).getValue().compareTo(val)>0)return ub;
            else return ub+1;
    
    }
    
    public int getCode(String val){
        int ind = getIndex(val);
        if(ind!=-1)return Vocab.get(ind).getCode();
        return -1;
    }

    public int getIndex(String val){
        int ind = getIndx(val);
        if(ind>Vocab.size()||ind==-1)return -1;
        if(ind!=0&&Vocab.get(ind-1).getValue().compareTo(val)==0)return ind-1;
        if(ind!=Vocab.size()&&Vocab.get(ind).getValue().compareTo(val)==0)return ind;
        return -1;
    }

    @Override
    public String toString(){
        String ret="";
        for(int i=0; i<Vocab.size(); i++)
            ret=ret+Vocab.get(i).getCode()+": "+Vocab.get(i).getValue()+"\n";
        return ret;
    }
    
    public String getValue(int code){
        for(int i =0; i<Vocab.size(); i++)
            if(Vocab.get(i).getCode()==code)return Vocab.get(i).getValue();
        return "no value with such code";
    }
    
}
