package beans;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


public class BrukerBean implements Serializable{
    private String brukernavn;
    private String rolle;
    private static Logger log = Logger.getLogger("tillerlopet");
    private String newPassword, confirmedNewPassword;
    private boolean changePWD;
    
    
    public String getConfirmedNewPassword() {return confirmedNewPassword;}
    public String getNewPassword() {return newPassword;}
    public String getRolle(){return rolle == null ? "" : rolle;}
    public boolean isChangePWD() {return changePWD;}
    public void setRolle(String rolle){ this.rolle = rolle;}
    public void setConfirmedNewPassword(String confirmedNewPassword) {this.confirmedNewPassword = confirmedNewPassword;}
    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}
    public String toggleChangePassword(){ 
        changePWD = !changePWD;
        System.out.println(changePWD ? "cpwd: ON":"cpwd:OFF");
        return "changed";
    }
    
    public boolean isActive(){
        if(brukernavn == null) return false;
        return true;
    }
    
    public String getBrukernavn(){
        if(brukernavn == null) getUserData();
        return brukernavn == null ? "" : brukernavn;
    }
    
    public boolean isInRolle(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        Object rqObj = ctx.getRequest();
        
        if(!(rqObj instanceof HttpServletRequest)) {
            log.severe("isInRolle(): Request er av typen "+ rqObj.getClass());
            return false;
        }
        HttpServletRequest rq = (HttpServletRequest) rqObj;
        return rq.isUserInRole(rolle);
    }

    private void getUserData(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        Object rqObj = ctx.getRequest();
        
        if(!(rqObj instanceof HttpServletRequest)) {
            log.severe("getUserData(): Request er av typen "+ rqObj.getClass());
            return;
        }
        HttpServletRequest rq = (HttpServletRequest) rqObj;
        brukernavn = rq.getRemoteUser();
    }
    
    public String changePassword(){
        pdc.DSConnect c = new pdc.DSConnect();
        if(!newPassword.equals(confirmedNewPassword))
            return "ulike_passord";
        else if(!changePWD) return "endring_ikke_tillatt";
        toggleChangePassword();
        return c.changePassword(brukernavn, newPassword) ? "passord_endret" : "feil_ved_endring";
    }
}
