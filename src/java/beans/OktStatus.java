/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Frode
 */
public class OktStatus {
    private Treningsokt okten;
    private boolean skalSlettes;
    
    public OktStatus(Treningsokt okten){
        this.okten = okten;
        skalSlettes = false;
    }
    public OktStatus(){
        okten = new Treningsokt();
        skalSlettes = false;
    }
    
    public synchronized boolean getSkalSlettes(){
        return skalSlettes;
    }
    public void setSkalSlettes(boolean nySkalSlettes){
        skalSlettes = nySkalSlettes;
    }
    public Treningsokt getOkten(){
        return okten;
    }
    public void setOkten(Treningsokt nyOkt){
        okten = nyOkt;
    }
}
