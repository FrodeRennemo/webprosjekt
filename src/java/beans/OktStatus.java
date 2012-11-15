/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Date;

/**
 *
 * @author Frode
 */
public class OktStatus {

    private Treningsokt okten;
    private boolean skalSlettes;
    private boolean endre;

    public OktStatus(Treningsokt okten) {
        this.okten = okten;
        skalSlettes = false;
        endre = false;
    }

    public OktStatus() {
        okten = new Treningsokt();
        skalSlettes = false;
        endre = false;
    }

    public synchronized boolean getSkalSlettes() {
        return skalSlettes;
    }

    public void setSkalSlettes(boolean nySkalSlettes) {
        skalSlettes = nySkalSlettes;
    }

    public Treningsokt getOkten() {
        return okten;
    }

    public void setOkten(Treningsokt nyOkt) {
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
