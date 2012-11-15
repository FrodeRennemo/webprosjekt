/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Oversikt implements Serializable {
    private ArrayList<Treningsokt> tabell = new ArrayList();
    private Database database = new Database(); 

    public Oversikt() {
        this.tabell = database.lesInn();
    }

    public Database getDatabase() {
        return database;
    }
    

    public void lesFraBruker() {
        database.lesInnBruker();
    }

    public ArrayList<Treningsokt> getAlleOkter() {
        return tabell;
    }

//    public int finnNokkel(Treningsokt Okt) {
//        return database.finnOktNr(Okt, bruker);
//    }

    public boolean registrerNyOkt(Treningsokt okt) {
        if (okt != null) {
            if (database.regNyOkt(okt)) {
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
        if (database.slettOkt(okten)) {
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
            if(okt.getNummer() == tabell.get(i).getNummer() && database.endreData(okt)){
                System.out.println(okt.getBeskrivelse());
                tabell.set(i, okt);
            }
        }
    }

    public String getBruker() {
        return database.getBruker();
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

    public static void main(String[] args) {
        Oversikt liste = new Oversikt();
        System.out.println(liste.toString());
        Treningsokt a = new Treningsokt(new java.util.Date(), 45, "BEEF", "aerobics");
        System.out.println(liste.registrerNyOkt(a));
        System.out.println(liste.toString());
//        System.out.println(liste.finnNokkel(a));
    }
}
