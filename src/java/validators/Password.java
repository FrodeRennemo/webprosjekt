package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class Password implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String passord = o.toString();
        
        boolean size = false;
        boolean spesialtegn = false;
        boolean tall = false;
        
        if(passord.length() >= 6){
            size = true;
            int i = 0;
            while((!tall || !spesialtegn) && i < passord.length()){
                if(!tall && Character.isDigit(passord.charAt(i))) tall = true;
                if(!spesialtegn && !Character.isLetterOrDigit(passord.charAt(i))) spesialtegn = true;
                i++;
            }
        }
        if(!size){
            FacesMessage message = language.Message.getMessage("language.auth", "password_short");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        if(!spesialtegn){
            FacesMessage message = language.Message.getMessage("language.auth", "password_special");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        if(!tall){
            FacesMessage message = language.Message.getMessage("language.auth", "password_number");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        
    }
    
}
