/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Spillstep {
        public IntVar tr,trt,ld,ldt,dz,dzt,sndr,sndt;
        public int time;


    public Spillstep(Model model, int tim) {
        tr = model.intVar("truck", 0,7,false);
        trt = model.intVar("trucktime", 0,360,true);
        ld = model.intVar("loader", 0,5,false); 
        ldt = model.intVar("loadertime", 0,360,true);
        dz = model.intVar("bulldozer", 0,5,false);
        dzt = model.intVar("bulldozertime", 0,360,true);
        sndr = model.intVar("sandinroad", 0, 2000, true);
        sndt = model.intVar("sandtotaldelivered", 0, 2000, true);
        time=tim;
    }

}
