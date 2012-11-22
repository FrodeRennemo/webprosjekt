package beans;

import database.Database;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import user.User;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private User user = new User();
    private String newPassword;
    private String repeatPassword;
    private String specialLetter = "-_,.";
    private Database database = new Database();
    private boolean passwordOk = false;
    private boolean userOk = false;

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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public boolean getUserOk() {
        return userOk;
    }

    public void setUserOk(boolean userOk) {
        this.userOk = userOk;
    }

    public String changePassword() {
        checkPassword();
        if (database.logIn(user)) {
            if (passwordOk) {
                user.setPassword(newPassword);
                if (database.changePassword(user)) {
                    return "passValid";
                }
            }
        }
        return null;
    }

    private void checkPassword() {
        if (newPassword.equals(repeatPassword)) {
            passwordOk = true;
        } else {
            passwordOk = false;
        }
    }

    private boolean checkUser() {
        if (!(database.userExist(user))) {
            userOk = true;
            return true;
        } else {
            return false;
        }
    }

    public String newUser() {
        if (checkUser()) {
            if (userOk) {
                checkPassword();
                if (passwordOk) {
                    user.setPassword(newPassword);
                    if (database.newUser(user)) {
                        return "userValid";
                    }
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("fail",new FacesMessage("Brukernavnet er i bruk"));
        }
        return null;

    }
}
