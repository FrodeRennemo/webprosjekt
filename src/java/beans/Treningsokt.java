package beans;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
@Named("Okt")
@SessionScoped
public class Treningsokt implements Serializable {

    private static int oktnummer = 0;
    private Date dato = new Date();
    private int varighet;
    private String beskrivelse;
    private String kategori;

    public Treningsokt() {
      
    }
    public void oppdatOktnummer(){
        oktnummer++;
    }

    public int getOktnummer() {
        return oktnummer;
    }

    public void setKategori(String kategori) {
        try {
            this.kategori = kategori;
        } catch (IllegalArgumentException e) {
            System.out.println("Ugyldig verdi!");
        }
    }

    public String getKategori() {
        return kategori;
    }

    public Date getDato() {
        return dato;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        try {
            this.beskrivelse = beskrivelse;
        } catch (IllegalArgumentException e) {
            System.out.println("Ugyldig verdi!");
        }
    }

    public int getVarighet() {
        return varighet;
    }

    public void setDato(Date dato) {

        this.dato = dato;
    }

    public void setVarighet(int varighet) {
        try {
            this.varighet = varighet;
        } catch (NumberFormatException e) {
            System.out.println("Må skrive inn heltall!");
        }
    }
}
