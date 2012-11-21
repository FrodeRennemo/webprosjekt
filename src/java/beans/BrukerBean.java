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

    private Bruker bruker = new Bruker();
    private String nyPassord;
    private String gjentaPassord;
    private String resultat = "";
    private String spesialtegn = "-_,.";
    private Database database = new Database();
    private boolean passordOk = false;

    public void setBrukernavn(String brukernavn) {
        bruker.setBrukernavn(brukernavn);
    }

    public void setNyPassord(String nyPassord) {
        this.nyPassord = nyPassord;
    }

    public String getNyPassord() {
        return nyPassord;
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

    public boolean getPassordOk() {
        return passordOk;
    }

    public void setPassordOk(boolean passordOk) {
        this.passordOk = passordOk;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getGjentaPassord() {
        return gjentaPassord;
    }

    public String skiftPassord() {
        sjekkPassord();
        if (database.loggInn(bruker)) {
            if (nyPassord.equals(gjentaPassord) && passordOk) {
                bruker.setPassord(nyPassord);
                if (database.endrePassord(bruker)) {
                    return "passordOk";
                }
            }
        }
        System.out.println("0");
        return resultat;


    }

    public void sjekkPassord() {
        int antallspestegn = 0;
        int antallsiffer = 0;
        int antbokstaver = 0;
        int antalltegn = 0;
        if (nyPassord.equals("")) {
            resultat = "Vennligst skriv inn et passord.";
            return;
        }
        if (!nyPassord.equals(gjentaPassord)) {
            resultat = "Passordene må være like.";
            return;
        }

        for (int i = 0; i < nyPassord.length(); i++) {
            char tegn = nyPassord.charAt(i);

            /* isLetterOrDigit() bruker tegnsettet som maskinen er satt opp med */
            if (!(Character.isLetterOrDigit(tegn)) && !(spesialtegn.indexOf(tegn) >= 0)) {
                resultat = "Spesialtegn";
                return;
            }
            if (Character.isLetter(tegn)) {
                antbokstaver++;
            }
            if (Character.isDigit(tegn)) {
                antallsiffer++;
            }
            if (spesialtegn.indexOf(tegn) >= 0) {
                antallspestegn++;
            }
            antalltegn++;
        }
        if (antallspestegn > 0 && antallsiffer > 0 && antallspestegn > 0 && antalltegn >= 6) {
            passordOk = true;
        } else {
            resultat = "For kort eller ikke nok bokstaver, tall eller spesialtegn";
        }
    }
}
