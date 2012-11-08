package beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;


@Named
@SessionScoped
public class Oktbehandler implements java.io.Serializable {
    private Oversikt oversikt = new Oversikt("tore");
    private List<OktStatus> tabelldata = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Treningsokt tempOkt = new Treningsokt(); // midlertidig lager for ny transaksjon
    private int maaned;
    private List<MaanedStatus> maaneddata = Collections.synchronizedList(new ArrayList<MaanedStatus>());

    public Oktbehandler() {
        if (oversikt.getTabell() != null) {
            for (int i = 0; i < oversikt.getTabell().size(); i++) {
                tabelldata.add(new OktStatus(oversikt.getTabell().get(i)));
            }
        }
    }

    public synchronized boolean getDatafins() {
        return (tabelldata.size() > 0);
    }

    public Oversikt getOversikt() {
        return oversikt;
    }

    public synchronized List<MaanedStatus> getMaaneddata() {
        return maaneddata;
    }

    public synchronized boolean getDataMaanedfins() {
        return (maaneddata.size() > 0);
    }

    public synchronized List<OktStatus> getTabelldata() {
        return tabelldata;
    }

    public synchronized void setMaaned(int maaned) {
        this.maaned = maaned;
    }

    public synchronized int getMaaned() {
        return maaned;
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

    public synchronized void leggTil() {
        Treningsokt nyOkt = new Treningsokt(tempOkt.getDato(), tempOkt.getVarighet(), tempOkt.getBeskrivelse(), tempOkt.getKategori());
        if (oversikt.registrerNyOkt(nyOkt)) {
            tabelldata.add(new OktStatus(nyOkt));
            tempOkt.nullstill();
        }
    }

    public synchronized void slett() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            OktStatus ts = tabelldata.get(indeks);
            if (ts.getSkalSlettes() && oversikt.slettOkt(ts.getOkten())) {
                tabelldata.remove(indeks);
            }
            indeks--;
        }
    }

    public synchronized double getSnittVarighet() {
        return oversikt.getSnittVarighet();
    }

    public synchronized void getAlleEnMnd() {
        maaneddata = Collections.synchronizedList(new ArrayList<MaanedStatus>());
        for (Treningsokt i : oversikt.getAlleOkterEnMnd(maaned)) {
            maaneddata.add(new MaanedStatus(i));
        }
    }

    public synchronized void endre() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            OktStatus ts = tabelldata.get(indeks);
            ts.setEndre(false);
            oversikt.endreData(ts.getOkten());
            indeks--;
        }
    }
}
