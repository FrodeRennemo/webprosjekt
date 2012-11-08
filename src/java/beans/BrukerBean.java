/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Frode
 */
@Named 
@SessionScoped
public class BrukerBean implements Serializable{
    private Bruker tempBruker;
    private Database database = new Database();
    private boolean login = false;
    
    public void setBrukernavn(String brukernavn) {
        tempBruker.setBrukernavn(brukernavn);
    }

    public void setPassord(String passord) {
        tempBruker.setPassord(passord);
    }

    public String getBrukernavn() {
        return tempBruker.getBrukernavn();
    }

    public String getPassord() {
        return tempBruker.getPassord();
    }
    public boolean loggInn(){
        if(database.logInn(tempBruker)){
            login = true;
            return true;
        }
        return false;
    }
    public boolean nyBruker(){
        if(database.nyBruker(tempBruker)){
            return true;
        }
        return false;
    }
    
    
    
}
