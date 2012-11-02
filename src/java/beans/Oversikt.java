/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Oversikt implements Serializable {
    int antOkter = 0;
    private String bruker = null;
    private ArrayList<Treningsokt> tabell = new ArrayList();
    private Database database = new Database("jdbc:derby://localhost:1527/waplj_prosjekt;user=waplj;password=waplj");

    public Oversikt(String bruker) throws SQLException {

        this.bruker = bruker;
        //if(database.lesInn(bruker)!=null){
        //     this.tabell = database.lesInn(bruker);
        //}
        this.tabell = database.lesInn();
    }

    public Oversikt() throws SQLException {
        this.tabell = database.lesInn();
    }

    public ArrayList<Treningsokt> getAlleOkter() {
        return tabell;
    }

    public boolean registrerNyOkt(Treningsokt okt) {
        if (okt != null) {
            System.out.println("ok");
            if(database.regNyOkt(okt, bruker)){
                tabell.add(okt);
                antOkter++;
                return true;
            }
        }
        return false;
    }

       

    public ArrayList<Treningsokt> getAlleOkterEnMnd(int maaned) {
        ArrayList<Treningsokt> Maaned = new ArrayList<Treningsokt>();
        for (int i = 0; i < tabell.size(); i++) {
            if (tabell.get(i).getDato() != null) {
                if (tabell.get(i).getDato().getMonth()+1 == maaned) {
                    Maaned.add(tabell.get(i));
                }
            }
        }
        return Maaned;
    }


    public boolean slettOkt(Treningsokt okten) throws SQLException {
        int indeks = 0;
        for (int i = 0; i < tabell.size(); i++) {
            if (tabell.get(i).equals(okten)) {
                indeks = i + 1;
                tabell.remove(okten);
            }
        }
        if (database.slettOkt(indeks,bruker)) {
            return true;
        }
        return false;
    }

   

    public int getSnittVarighet() {
        int total = 0;
        for (int i = 0; i < tabell.size(); i++) {
            total += tabell.get(i).getVarighet();
        }
        return (total / antOkter);
    }

    public void endreData(Date dato, int varighet, String beskrivelse, String kategori) {
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

    public static void main(String[] args) throws SQLException {
        Oversikt liste = new Oversikt("anne");
   
        System.out.println(liste.toString());
        System.out.println(liste.registrerNyOkt(new Treningsokt(new java.util.Date(),45,"BEEF","BEEF")));
        


    }
}




    

  


    

    
