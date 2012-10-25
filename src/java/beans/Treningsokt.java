package beans;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
@Named("Okt")
@SessionScoped
public class Treningsokt implements Serializable {

    private static int oktnummer = 1;
    private Date dato = new Date();
    private int varighet;
    private String beskrivelse;
    private String kategori;

    public Treningsokt() {
    }

    public Treningsokt(Date dato,int varighet, String beskrivelse, String kategori) {
        this.dato = dato;
        this.varighet = varighet;
        this.beskrivelse = beskrivelse;
        this.kategori = kategori;
    }
    
    public synchronized void oppdatOktnummer(){
        oktnummer++;
    }

    public synchronized int getOktnummer() {
        return oktnummer;
    }

    public synchronized void setKategori(String kategori) {
        try {
            this.kategori = kategori;
        } catch (IllegalArgumentException e) {
            System.out.println("Ugyldig verdi!");
        }
    }

    public synchronized String getKategori() {
        return kategori;
    }

    public synchronized Date getDato() {
        return dato;
    }

    public synchronized String getBeskrivelse() {
        return beskrivelse;
    }

    public synchronized void setBeskrivelse(String beskrivelse) {
        try {
            this.beskrivelse = beskrivelse;
        } catch (IllegalArgumentException e) {
            System.out.println("Ugyldig verdi!");
        }
    }

    public synchronized int getVarighet() {
        return varighet;
    }

    public synchronized void setDato(Date dato) {

        this.dato = dato;
    }

    public synchronized void setVarighet(int varighet) {
        try {
            this.varighet = varighet;
        } catch (NumberFormatException e) {
            System.out.println("Må skrive inn heltall!");
        }
    }
    public synchronized void nullstill(){
        dato = null;
        varighet = 0;
        beskrivelse = null;
        kategori = null;
    }
}
