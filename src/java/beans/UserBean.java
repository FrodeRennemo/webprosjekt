
package beans;

import database.Database;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import user.User;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private User user = new User();
    private String newPassword;
    private String repeatPassword;
    private String result = "";
    private String specialLetter = "-_,.";
    private Database database = new Database();
    private boolean passwordOk = false;

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public boolean getPasswordOk() {
        return passwordOk;
    }

    public void setPasswordOk(boolean passwordOk) {
        this.passwordOk = passwordOk;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public String changePassword() {
        checkPassword();
        if (database.logIn(user)) {
            if (newPassword.equals(repeatPassword) && passwordOk) {
                if (database.changePassword(user)) {
                    return "passValid";
                }
            }
        }
        return result;
    }

    public void checkPassword() {
        int special = 0;
        int number = 0;
        int letter = 0;
        int symbol = 0;
        if (newPassword.equals("")) {
            result = "Vennligst skriv inn et password.";
            return;
        }
        if (!newPassword.equals(repeatPassword)) {
            result = "passwordene må være like.";
            return;
        }

        for (int i = 0; i < newPassword.length(); i++) {
            char tegn = newPassword.charAt(i);

            /* isLetterOrDigit() user tegnsettet som maskinen er satt opp med */
            if (!(Character.isLetterOrDigit(tegn)) && !(specialLetter.indexOf(tegn) >= 0)) {
                result = "Spesialtegn";
                return;
            }
            if (Character.isLetter(tegn)) {
                letter++;
            }
            if (Character.isDigit(tegn)) {
                number++;
            }
            if (specialLetter.indexOf(tegn) >= 0) {
                special++;
            }
            symbol++;
        }
        if (special > 0 && number > 0 && special > 0 && symbol >= 6) {
            passwordOk = true;
        } else {
            result = "Too short or not enough letters, number or special letters.";
        }
    }
}
