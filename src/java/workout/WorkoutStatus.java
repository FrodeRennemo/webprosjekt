/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package workout;

import java.util.Date;

/**
 *
 * @author Frode
 */
public class WorkoutStatus {

    private Workout okten;
    private boolean skalSlettes;
    private boolean endre;

    public WorkoutStatus(Workout okten) {
        this.okten = okten;
        skalSlettes = false;
        endre = false;
    }

    public WorkoutStatus() {
        okten = new Workout();
        skalSlettes = false;
        endre = false;
    }

    public synchronized boolean getSkalSlettes() {
        return skalSlettes;
    }

    public void setSkalSlettes(boolean nySkalSlettes) {
        skalSlettes = nySkalSlettes;
    }

    public Workout getOkten() {
        return okten;
    }

    public void setOkten(Workout nyOkt) {
        okten = nyOkt;
    }

    public synchronized boolean getEndre() {
        return endre;
    }

    public void setEndre(boolean endres) {
        endre = endres;
    }
    
    public Date getDato(){
        return okten.getDato();
    }
    
    public void setDato(Date dato){
        okten.setDato(dato);
    }

    public void endre() {
        if (!endre) {
            endre = true;
        }
    }
}
