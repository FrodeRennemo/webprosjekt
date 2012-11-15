package beans;

import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@ConversationScoped
@Named(value="vis")

public class visningsBean implements java.io.Serializable {

    private boolean sortTid = false;
    private boolean sortDefault = true;
    private boolean sortKlasse = false;

    public boolean isSortDefault() {
        return sortDefault;
    }

    public boolean isSortKlasse() {
        return sortKlasse;
    }

    public boolean isSortTid() {
        return sortTid;
    }

    public void setSortDefault(boolean sortDefault) {
        this.sortDefault = sortDefault;
    }

    public void setSortKlasse(boolean sortKlasse) {
        this.sortKlasse = sortKlasse;
    }

    public void setSortTid(boolean sortTid) {
        this.sortTid = sortTid;
    }
    
    public String tidSort(){
        sortDefault = false;
        sortKlasse = false;
        sortTid = true;
        return "sortert";
    }
    public String klasseSort(){
        sortDefault = false;
        sortKlasse = true;
        sortTid = false;
        return "sortert";
    }
    public String defaultSort(){
        sortDefault = true;
        sortKlasse = false;
        sortTid = false;
        return "sortert";
    }
    
    
    
}
