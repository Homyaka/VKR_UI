package Mysystem;

abstract class Activable {
    private boolean flagActive = true;
    private Decision dec = null;
    private int status;
    
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
