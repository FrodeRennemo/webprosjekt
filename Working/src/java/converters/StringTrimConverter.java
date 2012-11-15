package converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=beans.BrukerBean.class)
public class StringTrimConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        return string.trim();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
      return o.toString();
    }
    
  
    
}
