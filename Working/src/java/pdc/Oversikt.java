package pdc;

import java.util.ArrayList;
import beans.*;
import java.sql.Time;
import javax.faces.bean.ApplicationScoped;

//@ApplicationScoped
/**
 *
 * @author espen
 */
public class Oversikt {
        private ArrayList<lopBean> lopene = new ArrayList<lopBean>();
        private ArrayList<deltakerBean> deltakere = new ArrayList<deltakerBean>();
        public static ArrayList<String> klasser = new ArrayList<String>();

        //DBHandler db;
        DSConnect db;
        
        public Oversikt(String brukernavn, String passord){
            //db = new DBHandler("waplj_prosjekt",brukernavn,passord);
            db = new DSConnect();
            deltakere = db.hentDeltakere();
            lopene = db.hentLop();
            klasser = db.hentKlasser();
            
            if(deltakere == null || lopene == null || klasser == null){
                System.err.println("!!!!TOM DATABASE!!!!");
            }
            
            for(int i = 0; i < lopene.size(); i++){
                lopene.get(i).setDeltakere(db.hentResultat(i, deltakere));
//                System.out.println(db.hentResultat(i,deltakere).get(i).toString());
            }
            
        }
        
        
        public synchronized String addLop(lopBean lop){
        if(lop == null) return "ingen_lop_registert";        
        lopBean nyttLop = new lopBean(lop.getNavn(), lop.getDistanse(), lop.getBeskrivelse());

        if(nyttLop.getNavn().isEmpty() || nyttLop.getBeskrivelse().isEmpty() || nyttLop.getDistanse() == 0.0)
            return "feil_med_inndata";
        //if(lopene.contains(nyttLop)) return "finnes_fra_før";
        //for(lopBean l : lopene) if(l.getNavn().equals(nyttLop.getNavn())) return "løp_navn_finnes";

        if(lopene.add(nyttLop) && db.skrivLop(lop) > 0) return "registret";
        
        return "det skjedde ein feil, som ikkje er rett.";
        }
        
        public synchronized String addDeltaker(deltakerBean deltaker){
                    if(deltaker == null) return "lol";
                deltakerBean nyDb = new deltakerBean(deltaker.getFornavn(), deltaker.getEtternavn(),deltaker.getAdresse() , deltaker.getEpost(), deltaker.getKjonn(), deltaker.getFodselsaar());
                for(deltakerBean d : deltakere) 
                    if( d.getAdresse().equals(nyDb.getAdresse())        &&
                        d.getEpost().equals(nyDb.getEpost())            &&
                        d.getEtternavn().equals(nyDb.getEtternavn())    &&
                        d.getFornavn().equals(nyDb.getFornavn())        &&
                        d.getFodselsaar() == nyDb.getFodselsaar()       &&
                        d.getKjonn() == nyDb.getKjonn()
                    )   return "deltaker_finnes_i_db";
                
                
                if(deltakere.add(nyDb) && db.skrivDeltaker(nyDb) > 0) return "registrert";
                
                return "DRITT";
        }

        public synchronized String leggTilDeltakerILop(lopBean lB, deltakerBean dB, int startnr, int klasse){
            if(lB == null || dB == null ) return "Ingen verdi";
            startnr = lB.beregnStartnr(startnr);
            System.out.println("Bruker startnr: "+startnr);
            if(db.skrivDeltakerTilLop(deltakere.indexOf(dB), lopene.indexOf(lB), klasse, startnr) > 0)
                return lB.addDeltaker(startnr, dB, klasse);
            
            System.err.println("Kunne ikkje registrere resultat");
            return "ERROR, Oversikt.java:130";
        }

        public synchronized String registrereTidTilDeltakerILop(lopBean lB, java.sql.Time tid, int startnr){
            //if(tid.equals("mm:ss") || tid.length() != 5) return "feil_med_tid";
            LopDeltaker ld = lB.getDeltaker(startnr);
            if(ld == null) return "deltaker_finnes_ikkje";
            
            //java.sql.Time time = s2dTid(tid);
            if(db.skrivResultat(tid, startnr, lopene.indexOf(lB)+1) > 0)
                return "registrert";
                //ld.setTid(s2dTid(tid));
            
            return "FEIL! l.80 oversikt.jav";
        }
        
//Hjelpemetoder        
    private java.sql.Time s2dTid(String str){

        String sMin = str.substring((str.indexOf(':') -2), (str.indexOf(':') )); //tall før :
        String sSek = str.substring(str.indexOf(':')+1,str.indexOf(':')+3); //tall etter :

        long sek = Long.parseLong(sSek) + (Long.parseLong(sMin)*60);

        return new java.sql.Time(sek*1000);
    }
    

//GETS    
    public deltakerBean getDeltakerBeanByID(int id){
        return deltakere.get(id);
    }
    
    public lopBean getLopBeanByID(int id){
        return lopene.get(id);
    }
    
    public ArrayList<deltakerBean> getDeltakerListe(){ 
        ArrayList<deltakerBean> nyListe = new ArrayList<deltakerBean>();
        for(deltakerBean deltaker : deltakere)
            nyListe.add(deltaker);
        return nyListe;
    }
    
    public ArrayList<lopBean> getLopListe(){
        ArrayList<lopBean> nyListe = new ArrayList<lopBean>();
        for(lopBean lop : lopene)
            nyListe.add(lop);
        return nyListe;
    }
    

    
//Påkrevde metoda.
    public ArrayList<deltakerBean> finnAlleLøp(){
        return getDeltakerListe();
    }

    
    public ArrayList<LopDeltaker> finnAlleDeltakereEtLop(lopBean lop){
            return lop.getSortertListe();

    }
    public ArrayList<LopDeltaker> finnResultatLop(lopBean lop){
            return lop.getSortertListe();
    }

    public void registrerNyttLop(lopBean lop) { addLop(lop); }
    public void registrerNyDeltaker(deltakerBean deltaker) { addDeltaker(deltaker); }
    public void registrerDeltakerPaLop(lopBean lB, deltakerBean dB, int startnr, int klasse) {leggTilDeltakerILop(lB, dB, startnr, klasse); }
    public void registrerResultat(lopBean lb, java.sql.Time tid, int startnr){
        
        if(registrereTidTilDeltakerILop(lb, tid, startnr).equals("registrert")) System.out.println("Reg tid: (l,t,s)"+lb+tid+startnr);
    }
    public void endreResultat(lopBean lb, Time tid, int startnr){registrereTidTilDeltakerILop(lb, tid, startnr);}

    
    public String getKlasseById(int i){
        return klasser.get(i);
    }
    public ArrayList<String> getKlasser(){
        return klasser;
    }

    public void leggtilKlasse(String klasse){
        klasser.add(klasse);
    }
    
    public void resultatTid(int lop){
        getLopBeanByID(lop).setDeltakere(db.hentResultat(lop, deltakere, "tid"));
    }
    public void resultatKlasse(int lop){
        getLopBeanByID(lop).setDeltakere(db.hentResultat(lop, deltakere, "klasse_id"));
    }
    
    public Time avgTid(int lop){
        return db.hentSnittTid(lop);
    }

}

