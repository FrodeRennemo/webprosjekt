package beans;

import javax.inject.Named;
import java.sql.Time;
import pdc.Oversikt;

@Named(value = "lopDeltaker")
public class LopDeltaker implements java.io.Serializable, Comparable<LopDeltaker>{
    
    private int startnr;
    private deltakerBean deltaker;
    private int klasse;
    private Time tid;
    private boolean editable;
    
    public LopDeltaker(int startnr, deltakerBean deltaker, int klasse) {
        this.deltaker = deltaker;
        this.klasse = klasse;
        this.startnr = startnr;
    }
    public LopDeltaker(int startnr, deltakerBean deltaker, int klasse, Time tid){
        this.deltaker = deltaker;
        this.klasse = klasse;
        this.startnr = startnr;
        this.tid = tid;
    }
    public LopDeltaker() {}

    public deltakerBean getDeltaker() {
        return deltaker;
    }

    public int getKlasse() {
        return klasse;
    }
    public String getKlasseSt(){
        return Oversikt.klasser.get(klasse);
    }

    public Time getTid() {
        return tid;
    }

    public int getStartnr() {
        return startnr;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setStartnr(int startnr) {
        this.startnr = startnr;
    }

    public void setDeltaker(deltakerBean deltaker) {
        this.deltaker = deltaker;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public void setTid(Time tid) {
        this.tid = tid;
    }
    
    public String toggleEdit(){
        editable = !editable;
        return "registrert";
    }
    
    @Override
    public String toString(){
        String tid = (this.tid == null) ? "DNF": this.tid.getMinutes() + ":" + this.tid.getSeconds();
        return startnr + ": " + deltaker + ", " + klasse + ": "+tid;
    }

    @Override
    public int compareTo(LopDeltaker o) {
        if(o.getKlasse() == this.klasse){
            if(tid != null && o.getTid() != null){
                return o.getTid().compareTo(tid) * (-1);
            }
            if(o.getStartnr() > this.startnr) return -1;
            else if (o.getStartnr() == this.startnr) return 0;
            return 1;
        }
        Integer t = new Integer(this.klasse);
        Integer ld = new Integer(o.getKlasse()) ;
        return t.compareTo(ld);
    }
    
}
