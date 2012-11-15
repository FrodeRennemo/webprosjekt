package pdc;

import beans.LopDeltaker;
import beans.deltakerBean;
import beans.lopBean;
import javax.annotation.Resource;
import javax.faces.bean.RequestScoped;
import javax.sql.*;
import java.sql.*;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 *
 * @author espen
 */
@RequestScoped

public class DSConnect {
    @Resource(name="jdbc/tillerlopet") DataSource ds;     
    private Connection c;
    
    public DSConnect(){
        try{
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/tillerlopet");
        }catch(NamingException ne){
            System.out.println("Data lookup fail.\n"+ne);
        }
        
        
    }
    
    
    private void start(){
        try{
            if(ds == null){
                System.err.println("No connection, ds==null. Aborting.");
                System.exit(0);
            }
            c = ds.getConnection();
        }
        catch(SQLException se){
            System.err.println("Feil ved oppkobling til database, Avslutter.\n"+se);
            System.exit(0);
        }
    }
    private void end(){
        try{
            if(!c.isClosed()) c.close();
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje lukke ds\n"+se);
        }
    }

//Hente fra database    
    public ArrayList<deltakerBean> hentDeltakere(){
        System.out.println("Henter Deltakere fra DB");
        start();
        ArrayList<deltakerBean> dbeans = new ArrayList<deltakerBean>();
        try{
            PreparedStatement ps  = c.prepareStatement("SELECT fornavn, etternavn,adresse,epost,kjonn,fodselsaar FROM deltaker");
            ResultSet r = ps.executeQuery();

            while(r.next()){
                dbeans.add(
                    new deltakerBean(
                        r.getString("fornavn"),
                        r.getString("etternavn"),
                        r.getString("adresse"),
                        r.getString("epost"),
                        r.getString("kjonn").charAt(0),
                        r.getInt("fodselsaar")
                    )
                );
            }
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje hente deltakere fra database.\n"+se);
            return null;
        }
        finally{
            end();
        }
        return dbeans;
    }
    public ArrayList<String> hentKlasser(){
        System.out.println("Henter klasser fra DB");
        start();
        
        String q = "SELECT aldersinndeling, kjonn from klasse";
        
        ArrayList<String> klasser = new ArrayList<String>();
        try{
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery();
            
            while(r.next()){
                klasser.add(r.getString("kjonn") +" "+
                            r.getString("aldersinndeling"));
            }
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje hente klasser fra Database:n"+se);
            return null;
        }
        finally{
            end();
        }
        return klasser;
    }
    public ArrayList<LopDeltaker> hentResultat(int lop, ArrayList<deltakerBean> d){     //Default
        ++lop;
        System.out.println("Henter resultater for løp_id "+lop+" fra DB");
        
        start();
        String q = "SELECT startnr, tid, deltaker_id, lop_id,klasse_id FROM resultat WHERE lop_id="+lop;

        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();
        try{
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery();
            
            while(r.next()){
                ld.add(new LopDeltaker(
                        r.getInt("startnr"),
                        d.get(r.getInt("deltaker_id")-1),
                        r.getInt("klasse_id"), 
                        r.getTime("tid"))
                        );
            }
            return ld;
        }
        catch(SQLException se){
            System.err.println("Feil med henting av Resultat: \n"+se);
            return null;
        }
        finally{
            end();
        }
    }
    public ArrayList<LopDeltaker> hentResultat(int lop, ArrayList<deltakerBean> d, String sortering){
        ++lop;
        System.out.println("Henter resultater for løp_id "+lop+" fra DB med sortering på "+sortering);
        start();
        String q = "SELECT startnr, tid, deltaker_id, lop_id,klasse_id FROM resultat WHERE lop_id= "+lop+" ORDER BY "+sortering;
        
        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();
        try{
            PreparedStatement p = c.prepareStatement(q);
            //p.setInt(1, lop);
            //p.setString(2, sortering);
            
            ResultSet r = p.executeQuery();
            
            while(r.next()){
                ld.add(new LopDeltaker(
                        r.getInt("startnr"),
                        d.get(r.getInt("deltaker_id")-1),
                        r.getInt("klasse_id"), 
                        r.getTime("tid"))
                        );
            }
            return ld;
        }
        catch(SQLException se){
            System.err.println("Feil med henting av Resultat: \n"+se);
            return null;
        }
        finally{
            end();
        }
        
    }
    public ArrayList<lopBean> hentLop(){
        System.out.println("Henter løp fra DB");
        start();
        
        String q = "SELECT navn, distanse, beskrivelse FROM lop";
        ArrayList<lopBean> lb = new ArrayList<lopBean>();
        
        try{
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery();
            
            while(r.next()){
                lb.add(
                    new lopBean(
                        r.getString("navn"),
                        Float.parseFloat(r.getString("distanse")),
                        r.getString("beskrivelse")
                    ));
            }
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje hente løp fra database:\n"+se);
            return null;
        }
        finally{
            end();
        }
        return lb;
    }
    public Time hentSnittTid(int lop){
        ++lop; //justering db/arraylist
        System.out.println("Henter snitttid for løp_id "+lop);
        start();
        String q = "select tid from resultat where lop_id="+lop+" and tid is not null";
        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();

        try{
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery();

            long tid = 0;
            int antall =0;
            while(r.next()){
                tid += r.getTime("tid").getTime();
                antall++;
            }
            if(tid != 0) tid = tid/antall;
            return new Time(tid);

        }
        catch(SQLException se){
            System.err.println("Feil med henting av Resultat: \n"+se);
            return null;
        }
        finally{
            end();
        }     
    }    

//Skrive til database
    public int skrivLop(lopBean lb){
        System.out.println("Skriver løp til DB: "+lb.getNavn());
        
        start();
        
        try{
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO lop (navn,distanse,beskrivelse)"+
                "VALUES (?,?,?)");
            
            ps.setString(1, lb.getNavn());
            ps.setFloat(2, lb.getDistanse());
            ps.setString(3, lb.getBeskrivelse());
            //ps.setDate(4, lb.getDato());
            
            return ps.executeUpdate();
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje legge til løp i databasen:\n"+se);
            return -1;
        }
        finally{
            end();
        }
    }
    public int skrivDeltaker(deltakerBean d){
        System.out.println("Skriv deltaker til DB: "+d.getEpost());
        
        start();
        
        try{
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO deltaker (fornavn,etternavn,adresse,epost,fodselsaar,kjonn) "+
                    "VALUES (?,?,?,?,?,?)");
            ps.setString(1, d.getFornavn());
            ps.setString(2, d.getEtternavn());
            ps.setString(3, d.getAdresse());
            ps.setString(4, d.getEpost());
            ps.setInt(5, d.getFodselsaar());
            ps.setString(6, d.getKjonn()+"");
        
            return ps.executeUpdate();
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje legge til deltaker:\n"+se);
            return -1;
        }
        finally{
            end();
        }
    }    
    public int skrivDeltakerTilLop(int deltaker, int lop, int klasse, int startnr){
        System.out.println("Skriv deltaker til DB");
        
        start();
        
        try{
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO resultat (startnr, deltaker_id, lop_id, klasse_id) "+
                    "VALUES (?,?,?,?)");
            ps.setInt(1, startnr);
            ps.setInt(2, ++deltaker);
            ps.setInt(3, ++lop);
            ps.setInt(4, ++klasse);
            
            return ps.executeUpdate();
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje legge deltaker til løp: \n"+se);
            return -1;
        }
        finally{
            end();
        }
    }
    public int skrivResultat(Time tid, int startnr, int lop){
        System.out.println("Skriv resultat til DB");
        
        start();

        try{
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE resultat "+
                    "SET tid = ? " +
                    "WHERE startnr = ? AND lop_id = ?");

            ps.setTime(1, tid);
            ps.setInt(2, startnr);
            ps.setInt(3, lop);
            return ps.executeUpdate();
                
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje registrer resultat: \n"+se);
            return -1;
        }
        finally{
            end();
        }
    }    
    
//Brukeradministrasjon
    
    public boolean changePassword(String user, String password){
        System.out.println("Endrer passord for "+user);
        start();
        
        String q = "UPDATE brukere set passord = ? where bruker = ?";
        try{
            PreparedStatement p = c.prepareStatement(q);
            p.setString(1, password);
            p.setString(2, user);
            int r = p.executeUpdate();
            System.out.println("Endret "+p+" rekker");
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje sette nytt passord for"+user+": \n"+se);
            return false;
        }
        finally{
            end();
        }
        return true;
    }
    

}
