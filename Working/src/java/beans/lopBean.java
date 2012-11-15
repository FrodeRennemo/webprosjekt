package beans;

import java.util.ArrayList;
import java.util.Collections;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author espen
 */
@Named(value = "lopBean")
@ConversationScoped
public class lopBean implements java.io.Serializable{

    private String navn;
    private float distanse;
    private String beskrivelse;
    private String dato;
    private ArrayList<LopDeltaker> deltakere = new ArrayList<LopDeltaker>();
    
    /** Creates a new instance of lopBean */
    public lopBean() {
    }

    public lopBean(String navn, float distanse, String beskrivelse) {
        this.navn = navn;
        this.distanse = distanse;
        this.beskrivelse = beskrivelse;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public float getDistanse() {
        return distanse;
    }

    public String getNavn() {
        return navn;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public void setDeltakere(ArrayList<LopDeltaker> deltakere) {
        this.deltakere = deltakere;
    }

    
    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setDistanse(float distanse) {
        this.distanse = distanse;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
    public String addDeltaker(int snr, deltakerBean deltaker, int klasse){
        LopDeltaker nyLD = new LopDeltaker(snr,deltaker,klasse);
        if(erRegistrert(nyLD)) return "deltaker_registrert_allerede";
        if(deltakere.add(nyLD)) return "visLop";
        return "det skjedde ein feil som ikkje er rett.";
    }
    public String addDeltaker(LopDeltaker ld){
        if(!deltakere.contains(ld)){
            deltakere.add(ld);    
            return "visLop";
        }
        return "det skjedde ein feil som ikkje er rett.";
    }

    public SelectItem[] getDeltakerListe(){
        SelectItem[] liste = new SelectItem[deltakere.size()];
        for(int i = 0; i < liste.length; i++)
            liste[i] = new SelectItem(deltakere.get(i));
        return liste;
    }
    
    public ArrayList<LopDeltaker> getDeltakerListeArray(){
        return deltakere;
    }
    
    public ArrayList<LopDeltaker> getSortertListe(){
        ArrayList<LopDeltaker> sortert = new ArrayList<LopDeltaker>();
        for(LopDeltaker ld : deltakere) sortert.add(ld);
        Collections.sort(sortert);
        return sortert;
    }
    
    
    @Override
    public String toString(){
        return navn + " " + distanse + "km";
    }
    
    public int beregnStartnr(int onske){
        if(onske < 0) onske = onske*-1;
        if(onske == 0) onske++;
        for(LopDeltaker ld : deltakere){
            System.out.println(ld.getStartnr()+" == "+onske);
            if(ld.getStartnr() == onske){
                beregnStartnr(++onske);
            }
        }
        return onske;
    }
    
    public LopDeltaker getDeltaker(int startnr){
        for(LopDeltaker ld : deltakere)
            if(ld.getStartnr() == startnr) return ld;
        return null;
    }
    
    private boolean erRegistrert(LopDeltaker ld){
        for(LopDeltaker l : deltakere)
            if(l.getDeltaker().equals(ld.getDeltaker())) return true;
        return false;
    }
}
