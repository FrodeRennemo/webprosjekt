package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author espen
 */

@FacesConverter(forClass=beans.LopDeltaker.class)
public class TidConverter implements Converter{
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String input) throws ConverterException{
        
        StringBuilder builder = new StringBuilder(input);
        boolean ugyldig = false;
        
        if(!(builder.charAt(2) == ':')) ugyldig = true;
        
        int minutt = 0;
        char m1 = builder.charAt(0);
        char m2 = builder.charAt(1);
        
        int sekund = 0;
        char s1 = builder.charAt(3);
        char s2 = builder.charAt(4);
        
        if(Character.isDigit(m1) && Character.isDigit(m2)) minutt = Integer.parseInt(input.substring(0, 2));
        else ugyldig = true;
        if(Character.isDigit(s1) && Character.isDigit(s2) ){
            if(Character.getNumericValue(s1) < 6) sekund = Integer.parseInt(input.substring(3,5));
            else ugyldig = true;
        }   else ugyldig = true;
        
        if(ugyldig) throw new ConverterException("Feil med oppgitt tid / Error with time.");
                
        return new java.sql.Time(0, minutt, sekund);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException{
        String input = value.toString();
        
        return input.substring(3);
        
    }
}
