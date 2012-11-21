/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author Frode
 */
public class User {
    private String brukernavn;
    private String passord;

    public User(String brukernavn, String passord) {
        this.brukernavn = brukernavn;
        this.passord = passord;
    }

    public User() {
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public String getPassord() {
        return passord;
    }
    
}
