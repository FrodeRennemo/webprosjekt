/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Oversikt implements Serializable {

    private String bruker = null;
    private int antOkter = 0;
    private ArrayList<Treningsokt> tabell = new ArrayList();

    public Oversikt(String bruker) {
        this.bruker = bruker;
    }

    public Oversikt() {
    }

    public ArrayList<Treningsokt> getAlleOkter() {
        return tabell;
    }

    public void registrerNyOkt(Treningsokt okt) {
        if (okt != null) {
            antOkter++;
            tabell.add(okt);
            okt.oppdatOktnummer();
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

    public void slettOkt(Treningsokt okten) {
        tabell.remove(okten);
    }

    public int getAntOkter() {
        return antOkter;
    }

    public int getSnittVarighet() {
        int total = 0;
        for (int i = 0; i < tabell.size(); i++) {
            total += tabell.get(i).getVarighet();
        }
        return (total / antOkter);
    }
    
    public void endreData(Date dato, int varighet, String beskrivelse, String kategori){
        
    }
}
