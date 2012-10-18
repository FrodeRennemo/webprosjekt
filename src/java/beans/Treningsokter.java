/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("Okter")
@SessionScoped
public class Treningsokter implements Serializable {

    private ArrayList<Treningsokt> tabell = new ArrayList();
    private Treningsokt nyOkt = new Treningsokt();

    public ArrayList<Treningsokt> getTabell() {
        return tabell;
    }

    public void setDato(Date dato) {
        nyOkt.setDato(dato);
    }

    public Treningsokt getNyOkt() {
        return nyOkt;
    }

    public void setNyOkt(Treningsokt nyOkt) {
        this.nyOkt = nyOkt;
    }
    public Date getDato(){
        return nyOkt.getDato();
    }
    public String getKategori(){
        return nyOkt.getKategori();
    }
    public int getOktnummer(){
        return nyOkt.getOktnummer();
    }

    public void setVarighet(int varighet) {
        nyOkt.setVarighet(varighet);
    }
    public int getVarighet(){
        return nyOkt.getVarighet();
    }

    public void setBeskrivelse(String beskrivelse) {
        nyOkt.setBeskrivelse(beskrivelse);
    }
    
    public String getBeskrivelse(){
        return nyOkt.getBeskrivelse();
    }
    
     public void leggTil() {
        //if (nyOkt.getBeskrivelse() != null && nyOkt.getDato() != null && nyOkt.getKategori() != null && nyOkt.getVarighet() > 0) {
            tabell.add(nyOkt);
            nyOkt.oppdatOktnummer();
       // }

    }
     public void setKategori(String nyKat){
         nyOkt.setKategori(nyKat);
     }
}
