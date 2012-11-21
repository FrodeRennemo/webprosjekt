package beans;

import workout.WorkoutStatus;
import workout.Workouts;
import workout.Workout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import workout.MonthStatus;
import workout.WorkoutStatus;
import workout.Workouts;
import workout.Workout;

@Named
@SessionScoped
public class WorkoutBean implements java.io.Serializable {

    private Workouts oversikt = new Workouts();
    private List<WorkoutStatus> tabelldata = Collections.synchronizedList(new ArrayList<WorkoutStatus>());
    private Workout tempOkt = new Workout(); // midlertidig lager for ny transaksjon
    private int maaned;
    private List<MonthStatus> maaneddata = Collections.synchronizedList(new ArrayList<MonthStatus>());

    public WorkoutBean() {
        if (oversikt.getTabell() != null) {
            for (int i = 0; i < oversikt.getTabell().size(); i++) {
                tabelldata.add(new WorkoutStatus(oversikt.getTabell().get(i)));
            }
        }
    }

    public synchronized boolean getDatafins() {
        return (tabelldata.size() > 0);
    }

    public Workouts getOversikt() {
        return oversikt;
    }

    public synchronized List<MonthStatus> getMaaneddata() {
        return maaneddata;
    }

    public synchronized boolean getDataMaanedfins() {
        return (maaneddata.size() > 0);
    }

    public synchronized List<WorkoutStatus> getTabelldata() {
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

    public synchronized Workout getTempOkt() {
        return tempOkt;
    }

    public synchronized void setTempOkt(Workout nyOkt) {
        tempOkt = nyOkt;
    }

    public synchronized void leggTil() {
        Workout nyOkt = new Workout(tempOkt.getDato(), tempOkt.getVarighet(), tempOkt.getBeskrivelse(), tempOkt.getKategori());
        if (oversikt.registrerNyOkt(nyOkt)) {
            tabelldata.add(new WorkoutStatus(nyOkt));
            if (nyOkt.getDato() == null) {
                nyOkt.setDato(new java.sql.Date(new java.util.Date().getTime()));
            }
            tempOkt.nullstill();
        }
    }

    public synchronized void slett() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            WorkoutStatus ts = tabelldata.get(indeks);
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
        maaneddata = Collections.synchronizedList(new ArrayList<MonthStatus>());
        for (Workout i : oversikt.getAlleOkterEnMnd(maaned)) {
            maaneddata.add(new MonthStatus(i));
        }
    }

    public synchronized void endre() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            WorkoutStatus ts = tabelldata.get(indeks);
            if (ts.getDato() == null) {
                ts.setDato(new java.sql.Date(new java.util.Date().getTime()));
            }
            ts.setEndre(false);
            oversikt.endreData(ts.getOkten());
            indeks--;
        }
    }

    public String logout() {
        // FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "../index.xhtml?faces-redirect=true";
    }
}
