package language;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class Message {
    
    public static FacesMessage getMessage(String bundle, String ressurs){
        
    FacesContext ctx = FacesContext.getCurrentInstance();
    UIViewRoot vRoot = ctx.getViewRoot();
    Locale locale = vRoot.getLocale();
    
    ClassLoader cloader = Thread.currentThread().getContextClassLoader();
    ResourceBundle rb = ResourceBundle.getBundle(bundle,locale,cloader);
    
    String res = rb.getString(ressurs);
    
    return new FacesMessage(res);  
    }
}
