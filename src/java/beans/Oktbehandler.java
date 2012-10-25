
package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped

public class Oktbehandler implements java.io.Serializable {

    private Treningsokter oversikt = new Treningsokter();
    private List<OktStatus> tabelldata = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Treningsokt tempOkt = new Treningsokt(); // midlertidig lager for ny transaksjon

    public synchronized boolean getDatafins() {  
        return (tabelldata.size() > 0);
    }

    public synchronized List<OktStatus> getTabelldata() {
        return tabelldata;
    }
    public synchronized Date getDato(){
        return oversikt.getDato();
    }
    public synchronized String getKategori(){
        return oversikt.getKategori();
    }
    public synchronized String getBeskrivelse(){
        return oversikt.getBeskrivelse();
    }
    public synchronized int getVarighet(){
        return oversikt.getVarighet();
    }
    
    
}
