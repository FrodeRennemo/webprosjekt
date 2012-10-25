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

    private String bruker = null;
    private int antOkter = 0;
    private ArrayList<Treningsokt> tabell = new ArrayList();
    private Treningsokt nyOkt = new Treningsokt();

    public Treningsokter() {
    }

    public Treningsokter(String bruker) {
        this.bruker = bruker;
    }

    public String getBruker() {
        return bruker;
    }

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

    public Date getDato() {
        return nyOkt.getDato();
    }

    public String getKategori() {
        return nyOkt.getKategori();
    }

    public int getOktnummer() {
        return nyOkt.getOktnummer();
    }

    public void setVarighet(int varighet) {
        nyOkt.setVarighet(varighet);
    }

    public int getVarighet() {
        return nyOkt.getVarighet();
    }

    public void setBeskrivelse(String beskrivelse) {
        nyOkt.setBeskrivelse(beskrivelse);
    }

    public String getBeskrivelse() {
        return nyOkt.getBeskrivelse();
    }

    public void registrerNyOkt() {
        if (nyOkt.getVarighet() > 0) {
            antOkter++;
            tabell.add(nyOkt);
            nyOkt.oppdatOktnummer();
        }

    }

    public ArrayList<Treningsokt> getAlleOkterEnMnd(int maaned) {
        ArrayList<Treningsokt> Maaned = new ArrayList<Treningsokt>();
        for (int i = 0; i < tabell.size(); i++) {
            if (tabell.get(i).getDato().getMonth() == maaned) {
                Maaned.add(tabell.get(i));
            }
        }
        return Maaned;

    }

    public boolean slettOkt(int oektnr) {
        for (int i = 0; i < tabell.size(); i++) {
            if (tabell.get(i).getOktnummer() == oektnr) {
                tabell.remove(i);
                antOkter--;
                return true;
            }

        }
        return false;
    }

    public void setKategori(String nyKat) {
        nyOkt.setKategori(nyKat);
    }

    public int getAntOkter() {
        return antOkter;
    }
    public int getSnittVarighet(){
        int total = 0;
        for(int i = 0;i<tabell.size();i++){
            total+=tabell.get(i).getVarighet();
        }
        return (total/antOkter);
    }
    
}
