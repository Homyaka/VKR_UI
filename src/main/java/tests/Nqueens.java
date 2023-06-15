/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import Mysystem.CodedValue;
import Mysystem.Column;
import Mysystem.Line;
import Mysystem.MySolution;
import Mysystem.Decisions;
import Mysystem.DSystem;
import Mysystem.Myvalue;
import Mysystem.Myvariable;
import Mysystem.Node;
import Mysystem.Valuevocab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Nqueens {

    public void build(int n, List<Myvariable> vars, Valuevocab domvals, List<DSystem> problems, List<MySolution> sols, Decisions decs){
        for(int i=0; i<n; i++)
            domvals.addValue(""+i);
        List<CodedValue> voc=domvals.getVocab();
        for(int i=0; i<n; i++){
            Myvariable nw = new Myvariable("Column "+(i+1));
            for(int j=0; j<n; j++)
                nw.addValue(new Myvalue(voc.get(j)));
            vars.add(nw);
        }
    
        for(int i=0; i<n-1; i++){
            for(int j=i+1; j<n; j++){
                DSystem newsys = new DSystem("System "+i+":"+j,sols,decs);
                problems.add(newsys);
                Column firstcol=new Column(vars.get(i).getDomain());
                vars.get(i).addColumn(firstcol);
                newsys.addColumn(firstcol);
                Column secondcol=new Column(vars.get(j).getDomain());
                vars.get(j).addColumn(secondcol);
                newsys.addColumn(secondcol);
                for(int ii=0; ii<n; ii++){
                    Node newnode;
                    Line thisline;
                    newnode = new Node(firstcol.getdomainwithout(voc.get(ii)));
                    thisline=new Line(); 
                    newsys.addLine(thisline);
                    thisline.addNode(newnode);
                    firstcol.addNode(newnode);
                    List<CodedValue> vals = new ArrayList<>();
                    vals.add(voc.get(ii));
                    if(ii-(j-i)>=0)vals.add(voc.get(ii-(j-i)));
                    if(ii+(j-i)<n)vals.add(voc.get(ii+(j-i)));
                    newnode = new Node(secondcol.getdomainwithout(vals));
                    thisline.addNode(newnode);
                    secondcol.addNode(newnode);
                }
            }   
        }
    }    
    
}
