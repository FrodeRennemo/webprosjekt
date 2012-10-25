
package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped

public class Oktbehandler implements java.io.Serializable {

    private Treningsokter oversikt = new Treningsokter();
    private List<OktStatus> tabelldata = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Treningsokt tempOkt = new Treningsokt(); // midlertidig lager for ny transaksjon

    public synchronized boolean getDatafins() {  
        return (tabelldata.size() > 0);
    }

    public synchronized List<OktStatus> getTabelldata() {
        return tabelldata;
    }
    public synchronized Date getDato(){
        return oversikt.getDato();
    }
    public synchronized void setDato(Date nyDato){
        oversikt.setDato(nyDato);
    }
    public synchronized String getKategori(){
        return oversikt.getKategori();
    }
    public synchronized void setKategori(String kategori){
        oversikt.setKategori(kategori);
    }
    public synchronized String getBeskrivelse(){
        return oversikt.getBeskrivelse();
    }
    public synchronized void setBeskrivelse(String beskrivelse){
        oversikt.setBeskrivelse(beskrivelse);
    }
    public synchronized int getVarighet(){
        return oversikt.getVarighet();
    }
    public synchronized void setVarighet(int varighet){
        oversikt.setVarighet(varighet);
    }
    public synchronized Treningsokt getTempOkt(){
        return tempOkt;
    }
    public synchronized void setTempOkt(Treningsokt nyOkt){
        tempOkt = nyOkt;
    }
      public synchronized void oppdater() {
    /* Ny transaksjon er midlertidig lagret i tempTrans */
    if (!tempOkt.getKategori().trim().equals("")) {
      /* Lagrer data om ny transaksjon permanent */
      Treningsokt nyOkt = new Treningsokt(tempOkt.getDato(),
                                tempTrans.getBeløp(), tempTrans.getTekst());
       nyTrans.setTransnr(Transaksjon.lagNyttTransnr());
       oversikt.registrerNyOkt(nyOkt);   // lagrer i problemdomeneobjekt
       tabelldata.add(new TransaksjonStatus(nyTrans)); // lagrer i presentasjonsobjektet
       tempTrans.nullstill();
    }
    /* Sletter alle transaksjoner som er merket for sletting */
    int indeks = tabelldata.size() - 1;
    while (indeks >= 0) {
      TransaksjonStatus ts = tabelldata.get(indeks);
      if (ts.getSkalSlettes()) {
        oversikt.slettTransaksjon(ts.getTransaksjonen()); // sletter i problemdomeneobjekt
        tabelldata.remove(indeks);  // sletter i presentasjonsobjektet
      }
      indeks--;
    }
  }
    
}
