package language;

import java.io.Serializable;
import java.util.Locale;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@Named(value="lc")
@SessionScoped
public class LocaleChanger implements Serializable{
    private String sprak= "no";
    
    public String getSprak(){
        return sprak;
    }
    
    public void setNorsk(){
        sprak = "no";
    }
        public void setEngelsk(){
        sprak = "en";
    }
    
}
