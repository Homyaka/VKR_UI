package Mysystem;

import java.util.ArrayList;
import java.util.List;

public class Decision {
    private List <Activable> changes;
    private String descr;
    private int status;
    
    public Decision(List <Activable> ch, String ds, int stat){
        changes = ch;
        descr = ds;
        status = stat;
    }

    public List<Activable> getChanges() {
        return changes;
    }

    public void apply(boolean dec){
        for(int i=0; i<changes.size(); i++)
            if(changes.get(i).isActive()!=dec)
                changes.get(i).activate(dec, this,status);
            else changes.remove(i);
    }

    public void unapply(boolean dec){
        for(int i=0; i<changes.size(); i++)
            changes.get(i).activate(!dec, null, status);
    }
    
    public String getDescr() {
        return descr;
    }

    public int getStatus() {
        return status;
    }
}
