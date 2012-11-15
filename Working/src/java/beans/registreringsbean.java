package beans;

import java.util.ArrayList;
import java.util.Calendar;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.inject.Inject;

@Named(value = "reg")
@ApplicationScoped

public class registreringsBean implements java.io.Serializable{
    @Inject private lopBean lop = new lopBean();
    @Inject private deltakerBean deltaker = new deltakerBean();
    @Inject private LopDeltaker ld = new LopDeltaker();

    pdc.Oversikt os = new pdc.Oversikt("waplj","waplj");
    
    //Arbeidsvariabler    
    protected int dB, lB, klasse;
    private int startnr = 1;
    private String tid;      //brukes for å vidersende klasse til LøpDeltaker
    
    /** Creates a new instance of registreringsBean */


    
    public void setDeltaker(deltakerBean deltaker) {
        this.deltaker = deltaker;
    }

    public void setLop(lopBean lop) {
        this.lop = lop;
    }
    public void setDb(int dB){
        this.dB = dB;
    }
    public void setLb(int lB){
        this.lB = lB;
    }
    
    public String getTid(){
        return tid;
    }
    
    public deltakerBean getDeltaker() {
        return deltaker;
    }

    public lopBean getLop() {
        return lop;
    }

    public void setTid(String tid){
        this.tid = tid;
    }
    
    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public void setStartnr(int startnr) {
        this.startnr = startnr;
    }

    public int getKlasse() {
        return klasse;
    }

    public int getStartnr() {
        return lop.beregnStartnr(startnr);
    }
    public int getDb(){
        return this.dB;
    }
    public int getLb(){
        return this.lB;
    }
    
//SELECT ITEM LISTE
    
    public SelectItem[] klasser(){
        SelectItem[] klasser = new SelectItem[os.getKlasser().size()];
        for(int i = 0; i < klasser.length; i++){
            String k = (i < 16) ? "Menn ":"Kvinner ";
            klasser[i] = new SelectItem(i, k + os.getKlasser().get(i));
        }            
        return klasser;
    }
    
    public SelectItem[] getDeltakerListe() {
        java.util.ArrayList<deltakerBean> deltakere = os.getDeltakerListe();
        SelectItem[] deltakerliste = new SelectItem[deltakere.size()];
        for(int i = 0; i < deltakere.size(); i++)
            deltakerliste[i] = new SelectItem(i, deltakere.get(i).toString());
        return deltakerliste;
    }

    public SelectItem[] getLopListe() {
        java.util.ArrayList<lopBean> lopene = os.getLopListe();
        SelectItem[] lopliste = new SelectItem[lopene.size()];
        for(int i = 0; i < lopene.size(); i++)
            lopliste[i] = new SelectItem(i, lopene.get(i).toString());
        
        return lopliste;
    }   

    public SelectItem[] getAarListe(){
        java.util.GregorianCalendar d = new java.util.GregorianCalendar();
        int startaar = 1900;
        int no = d.get(Calendar.YEAR);
        no++;
        SelectItem[] liste = new SelectItem[(no-startaar)];
        for(int i = 0; i < liste.length; i++)
            liste[i] = new SelectItem(i+startaar);
        return liste;
    }
    
        public String nyDeltaker(){
            return os.addDeltaker(deltaker);
    }

    public String regDeltakerTilLop(){
           //lopBean lop= os.getLopBeanByID(lB);
           System.out.println("lb: "+lB + ", dB: "+dB+", snr:"+startnr+", k: "+klasse);
           return os.leggTilDeltakerILop(os.getLopBeanByID(lB), os.getDeltakerBeanByID(dB), startnr, klasse);
    }
    
    public String nyttLop(){
        return os.addLop(lop);
    }
/*    public String regResultat(){
        System.out.println("lb:"+lB+", tid:"+tid+", starnr:"+startnr);
        return os.registrereTidTilDeltakerILop(os.getLopBeanByID(lB), tid, startnr);
    }
*/  
    public LopDeltaker[] getStartliste(){
        
        ArrayList<LopDeltaker> lds = os.finnAlleDeltakereEtLop(os.getLopBeanByID(lB));
        LopDeltaker[] startliste = new LopDeltaker[lds.size()];
        for(int i = 0; i < startliste.length; i++) startliste[i] = lds.get(i);
        
        return startliste;
        
    }
    public ArrayList<LopDeltaker> getStartlisteAL(){
        //return os.getLopBeanByID(lB).getSortertListe();
        return os.getLopBeanByID(lB).getDeltakerListeArray();
    }
    public String resultatTid(){
         os.resultatTid(lB);
         return "sortert";
    }
    public String resultatKlasse(){
         os.resultatKlasse(lB);
         return "sortert";
    }
    
    
    public int getAntallLopere(){
        return os.getLopBeanByID(lB).getDeltakerListeArray().size();
    }

    public String settLop(){
        lop = os.getLopBeanByID(lB);
        return "valt";
    }
    
    public String lagreResultatliste(){
        lopBean lopa = os.getLopBeanByID(lB);        
        for(LopDeltaker d : lopa.getDeltakerListeArray())
            os.registrerResultat(lopa, d.getTid(), d.getStartnr());
        
        return "registrert";
    }
    
    public String getLopNavn(){
        return os.getLopBeanByID(lB).getNavn();
    }
    
    public ArrayList<lopBean> getLopBeans(){
        return os.getLopListe();
    }
    public java.sql.Time avgTid(){
        return os.avgTid(lB);
    }
    
}
