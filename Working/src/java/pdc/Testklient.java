package pdc;

import beans.*;
/**
 *
 * @author espen
 */
public class Testklient {

    public static void main(String[] args) {
        
        Oversikt o = new Oversikt("jsf","jsf");

        System.out.println("KONSTRUKTØR FERDIG");
        
/*      o.registrerNyDeltaker(new deltakerBean("Espen","xx","c 15","espen@epost.no",'M',1988));
        o.registrerNyDeltaker(new deltakerBean("Jørn Erling","xx","a 4","jorn@epost.no",'M',1990));
        o.registrerNyDeltaker(new deltakerBean("Thomas","xx","d 15","thomas@epost.no",'M',1986));
        o.registrerNyDeltaker(new deltakerBean("Gunhild Marie","xx","b 4","gunhild@epost.no",'F',1988));
        o.registrerNyDeltaker(new deltakerBean("Ingvill", "xx", ".", "ingvill@epost.no",'F', 1990));
        o.registrerNyDeltaker(new deltakerBean("Erik", "xx","Gatevegen 23", "erik@epost.no",'M',1990));
    
        System.out.println("LAGT TIL DELTAKERE ");
  */      
        System.out.println("Deltakere registrert i systemet:");
        for(deltakerBean d :o.getDeltakerListe()) System.out.println(d.toString());
    /*    
        o.registrerNyttLop(new lopBean("Pensjonistlopet",1.2f,"Løp for pensjonister"));
        o.registrerNyttLop(new lopBean("Naboløpet",4.2f,"Løp for naboer"));
      */  
        System.out.println("\nLøp Registrert i systemet:");
        for(lopBean l : o.getLopListe()) System.out.println(l.toString());
        /*
        o.registrerDeltakerPaLop(o.getLopListe().get(0),o.getDeltakerListe().get(0),1,1);
        o.registrerDeltakerPaLop(o.getLopListe().get(0),o.getDeltakerListe().get(1),2,1);
        o.registrerDeltakerPaLop(o.getLopListe().get(0),o.getDeltakerListe().get(2),3,2);
        o.registrerDeltakerPaLop(o.getLopListe().get(0),o.getDeltakerListe().get(3),4,16);
        */
        System.out.println("\nDeltakere i "+o.getLopListe().get(0).toString());
        for(LopDeltaker l : o.finnAlleDeltakereEtLop(o.getLopListe().get(0))) System.out.println(l.toString());
        /*
        o.registrerDeltakerPaLop(o.getLopListe().get(1),o.getDeltakerListe().get(3),1,19);
        o.registrerDeltakerPaLop(o.getLopListe().get(1),o.getDeltakerListe().get(4),2,4);
        o.registrerDeltakerPaLop(o.getLopListe().get(1),o.getDeltakerListe().get(5),3,6);
        o.registrerDeltakerPaLop(o.getLopListe().get(1),o.getDeltakerListe().get(2),4,2);
        
        System.out.println("\nDeltakere i "+o.getLopListe().get(1).toString());
        for(LopDeltaker l : o.finnAlleDeltakereEtLop(o.getLopListe().get(1))) System.out.println(l.toString());
        
        o.registrerResultat(o.getLopListe().get(0),"10:23", 1);
        o.registrerResultat(o.getLopListe().get(0),"08:23", 2);
        o.registrerResultat(o.getLopListe().get(0),"07:23", 3);
        o.registrerResultat(o.getLopListe().get(0),"12:23", 4);
        */
        System.out.println("\nResultatliste for "+o.getLopListe().get(0).toString());
        for(LopDeltaker l : o.finnAlleDeltakereEtLop(o.getLopListe().get(0))) System.out.println(l.toString());
        /*
        o.registrerResultat(o.getLopListe().get(1),"10:23", 1);
        o.registrerResultat(o.getLopListe().get(1),"28:23", 2);
        o.registrerResultat(o.getLopListe().get(1),"17:23", 3);
        o.registrerResultat(o.getLopListe().get(1),"12:23", 4);
        
        System.out.println("\nResultatliste for "+o.getLopListe().get(1).toString());
        for(LopDeltaker l : o.finnAlleDeltakereEtLop(o.getLopListe().get(1))) System.out.println(l.toString());
      
        System.out.println("\nEndrer resultat for løpere");
        System.out.println("Endrer tiden for "+o.getLopListe().get(0).getDeltaker(1)+" i "+o.getLopListe().get(0)+" til 07:12" );
        o.registrerResultat(o.getLopListe().get(0),"07:12", 1);
        
        System.out.println("\nSkriver ut resultatliste på nytt");
        System.out.println("\nResultatliste for "+o.getLopListe().get(0).toString());
        for(LopDeltaker l : o.finnAlleDeltakereEtLop(o.getLopListe().get(0))) System.out.println(l.toString());
          */    
    }
    
}
