/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

abstract class Activable {
    private boolean flagActive = true;
    private Decision dec = null;
    private int status; //

    public int getStatus() {
        return status;
    }
    
    public void activate(boolean act, Decision dc, int st){
        if(flagActive !=act){
            flagActive =act;
            dec=dc;
            status=st;
            onActivate();
        }
    }
    abstract void onActivate();

    public boolean isActive(){return flagActive;}
    public Decision getDec(){return dec;}

}
