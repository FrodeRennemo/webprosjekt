package beans;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named(value = "deltakerBean")
@ConversationScoped
public class deltakerBean implements java.io.Serializable{

    private String fornavn;
    private String etternavn;
    private String adresse;
    private String epost;
    private char kjonn;
    private int fodselsaar;

    /** Creates a new instance of deltagerBean */
    public deltakerBean() {
    }

    public deltakerBean(String fornavn, String etternavn, String adresse, String epost, char kjonn, int fodselsaar) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.adresse = adresse;
        this.epost = epost;
        this.kjonn = kjonn;
        this.fodselsaar = fodselsaar;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getEpost() {
        return epost;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public int getFodselsaar() {
        return fodselsaar;
    }

    public String getFornavn() {
        return fornavn;
    }

    public char getKjonn() {
        return kjonn;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public void setFodselsaar(int fodselsaar) {
        this.fodselsaar = fodselsaar;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public void setKjonn(char kjonn) {
        this.kjonn = kjonn;
    }
    
    @Override
    public String toString(){
        return fornavn + " " + etternavn;
    }
}
