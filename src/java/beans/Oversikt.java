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
    private ArrayList<Treningsokt> tabell = new ArrayList();
    private Database database = new Database("jdbc:derby://localhost:1527/waplj_prosjekt;user=waplj;password=waplj");

    public Oversikt(String bruker) {
        this.bruker = bruker;
        this.tabell = database.lesInn();
    }

    public Oversikt() {
        this.tabell = database.lesInn();
    }

    public void lesFraBruker(String bruker) {
        database.lesInnBruker(bruker);
    }

    public ArrayList<Treningsokt> getAlleOkter() {
        return tabell;
    }

//    public int finnNokkel(Treningsokt Okt) {
//        return database.finnOktNr(Okt, bruker);
//    }

    public boolean registrerNyOkt(Treningsokt okt) {
        if (okt != null) {
            if (database.regNyOkt(okt, bruker)) {
                tabell.add(okt);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Treningsokt> getAlleOkterEnMnd(int maaned) {
        ArrayList<Treningsokt> Maaned = new ArrayList<Treningsokt>();
        for (int i = 0; i < tabell.size(); i++) {
            if (tabell.get(i).getDato() != null) {
                if (tabell.get(i).getDato().getMonth() + 1 == maaned) {
                    Maaned.add(tabell.get(i));
                }
            }
        }
        return Maaned;
    }

    public boolean slettOkt(Treningsokt okten) {
//        int oktNr = database.finnOktNr(okten, bruker);
        if (database.slettOkt(okten, bruker)) {
            tabell.remove(okten);
            return true;
        }
        return false;
    }

    public int getSnittVarighet() {
        int total = 0;
        for (int i = 0; i < tabell.size(); i++) {
            total += tabell.get(i).getVarighet();
        }
        if (tabell.isEmpty()) {
            return 0;
        }
        return (total / tabell.size());
    }

    public void endreData(Treningsokt okt) {
        
        for(int i = 0;i<tabell.size();i++){
            if(okt.getNummer() == tabell.get(i).getNummer() && database.endreData(okt, bruker)){
                System.out.println(okt.getBeskrivelse());
                tabell.set(i, okt);
            }
        }
    }

    public String getBruker() {
        return bruker;
    }

    @Override
    public String toString() {
        String utskrift = "";
        for (int i = 0; i < tabell.size(); i++) {
            utskrift += tabell.get(i).getDato() + " " + tabell.get(i).getVarighet() + " " + tabell.get(i).getKategori() + " " + tabell.get(i).getBeskrivelse() + "\n";
        }
        return utskrift;
    }

    public ArrayList<Treningsokt> getTabell() {
        return tabell;
    }

    public void setBruker(String bruker) {
        this.bruker = bruker;
    }

    public static void main(String[] args) {
        Oversikt liste = new Oversikt("tore");
        System.out.println(liste.toString());
        Treningsokt a = new Treningsokt(new java.util.Date(), 45, "BEEF", "aerobics");
        System.out.println(liste.registrerNyOkt(a));
        System.out.println(liste.toString());
//        System.out.println(liste.finnNokkel(a));
    }
}
