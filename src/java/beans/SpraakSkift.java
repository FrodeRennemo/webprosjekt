
package beans;

import java.io.Serializable;
import java.util.Locale;
import javax.el.PropertyNotFoundException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("Spraak")
@Dependent
public class SpraakSkift {

    private FacesContext context = FacesContext.getCurrentInstance();
    private Locale locale =  context.getViewRoot().getLocale();

    public Locale getLocale() {
        return locale;
    }
    public String getSpraak(){
        return locale.getLanguage();
    }

    private void setSpraak(String spraak) {
        locale  = new Locale(spraak);
        context.getViewRoot().setLocale(new Locale(spraak));
    }
    
    public void setEng(){
        setSpraak("en");
    }

    public void setNo(){
        setSpraak("no");
    }
}
