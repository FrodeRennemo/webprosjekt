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
public class BrukerBean implements Serializable {

    private Bruker bruker;
    private String gjentaPassord;
    private Database database = new Database();
    private boolean login = false;
    private boolean nybrukerOk = false;

    public void setBrukernavn(String brukernavn) {
        bruker.setBrukernavn(brukernavn);
    }

    public void setPassord(String passord) {
        bruker.setPassord(passord);
    }

    public String getBrukernavn() {
        return bruker.getBrukernavn();
    }

    public String getPassord() {
        return bruker.getPassord();
    }

    public void setGjentaPassord(String gjentaPassord) {
        this.gjentaPassord = gjentaPassord;
    }

    public String getGjentaPassord() {
        return gjentaPassord;
    }

    public boolean loggInn() {
        if (database.logInn(bruker)) {
            login = true;
            return true;
        }
        return false;
    }

    public boolean nyBruker() {
        if (bruker.getPassord().equals(gjentaPassord)) {
            if (database.nyBruker(bruker)) {
                nybrukerOk = true;
                return true;
            }
        }
        return false;
    }
}
