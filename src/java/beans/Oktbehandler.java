package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Oktbehandler implements java.io.Serializable  {
    
    private Oversikt oversikt = new Oversikt();
    private List<OktStatus> tabelldata = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Treningsokt tempOkt = new Treningsokt(); // midlertidig lager for ny transaksjon

    public synchronized boolean getDatafins() {
        return (tabelldata.size() > 0);
    }

    public synchronized List<OktStatus> getTabelldata() {
        return tabelldata;
    }

    public synchronized Date getDato() {
        return tempOkt.getDato();
    }

    public synchronized void setDato(Date nyDato) {
        tempOkt.setDato(nyDato);
    }

    public synchronized String getKategori() {
        return tempOkt.getKategori();
    }

    public synchronized void setKategori(String kategori) {
        tempOkt.setKategori(kategori);
    }

    public synchronized String getBeskrivelse() {
        return tempOkt.getBeskrivelse();
    }

    public synchronized void setBeskrivelse(String beskrivelse) {
        tempOkt.setBeskrivelse(beskrivelse);
    }

    public synchronized int getVarighet() {
        return tempOkt.getVarighet();
    }

    public synchronized void setVarighet(int varighet) {
        tempOkt.setVarighet(varighet);
    }

    public synchronized Treningsokt getTempOkt() {
        return tempOkt;
    }

    public synchronized void setTempOkt(Treningsokt nyOkt) {
        tempOkt = nyOkt;
    }

    public synchronized void oppdater() {
        if (!tempOkt.getKategori().trim().equals("")) {
            Treningsokt nyOkt = new Treningsokt(tempOkt.getDato(), tempOkt.getVarighet(), tempOkt.getBeskrivelse(), tempOkt.getKategori());
            oversikt.registrerNyOkt(nyOkt);
            tabelldata.add(new OktStatus(nyOkt));
            tempOkt.nullstill();
        }
    }

    public synchronized void slett() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            OktStatus ts = tabelldata.get(indeks);
            if (ts.getSkalSlettes()) {
                oversikt.slettOkt(ts.getOkten());
                tabelldata.remove(indeks);
            }
            indeks--;
        }
    }
    public synchronized void endre() {
        
    }
}
