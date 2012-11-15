package pdc;

import beans.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Jørn
 */
class DBHandler{
  private static Connection db = null;
  private String databasedriver = "org.apache.derby.jdbc.ClientDriver";
  private String dburl;
  
  
  public DBHandler(String db, String user, String pass){
    if(db != null || user != null || pass != null){
      try{
        Class.forName(databasedriver); // laster inn driverklassa
      }
      catch(ClassNotFoundException cnf){
        System.err.println("Driverfeil: Kunne ikkje laste driver! \n "+ cnf.toString());
        System.exit(0); //Ikkje vits å fortsette om databasen ikkje kan koblast opp...
      }
      
      dburl = "jdbc:derby://localhost:1527/"+db+";user="+user+";password="+pass+";";
      try{
        this.db = DriverManager.getConnection(dburl);
      }
      catch(SQLException se){
        System.err.println("Kunne ikkje kople til databasen "+db+" med bruker "+user+".\n" + se);
        System.exit(0);
      }
    }
    else{
      System.err.println(user+":"+pass+"@"+db);
      throw new IllegalArgumentException("Du har ikkje oppgitt bruker, passord eller database.");
    }
  }
  
  private void lukkForbindelse(){
    try{
      db.close();
    }
    catch(SQLException se){
      System.err.println("DB-feil: Feil ved lukking av databaseforbindelse:\n" + se);
      System.exit(0);
    }
  }
  
  private void openDB(){
     try{
      db = DriverManager.getConnection(dburl);   
     }
    catch(SQLException se) {
        System.err.println("Connection ERROR!!!! l.54\n"+se);
        System.exit(0);
    }
  }
  
  private void lukkRS(ResultSet res) {
    try {
      if (res != null) {
        res.close();
      }
    } catch (SQLException se) {
      System.err.println("DB-feil: Feil ved lukking av ResultSet\n" + se);
      System.exit(0);
    }
    finally{
        lukkForbindelse();
    }
  }
  
  private ResultSet nyRES(Statement stm, String query){
      ResultSet res = null;
      try{
        res = stm.executeQuery(query);    
      }
      catch(SQLException sqle){
          System.err.println("Kunne ikkje lage nytt ResultSet\n"+sqle);
          System.exit(0);
      }
      return res;
  }
  
  

  private  Statement nySTM(){
      openDB();
  Statement stm = null;
    try{
      stm = db.createStatement();
    }
    catch(SQLException se){
      System.err.println("DB-feil: Feil ved åpning av statement.\n"+ se);
      System.exit(0);
    }
    return stm;
  }
  
  public void lukkSTM(Statement stm) {
    try{
      if (stm != null) {
        stm.close();
      }
    }
    catch (SQLException se) {
      System.err.println("DB-feil: Feil ved lukking av Statement\n" + se);
      System.exit(0);
    }
  }
  
  private void lukkRSF(ResultSet res, Statement stm){
      lukkRS(res);
      lukkSTM(stm);
      lukkForbindelse();
  }
 
 public boolean sendInsert(String query, Statement stm){
     System.out.println("Sender SQL-query: "+query);
     openDB();
     
     
    try{
        stm.execute(query);
    }
    catch(SQLException sqle){
        System.err.println("Kunne ikkje utføre query:\n"+query+"\n"+sqle);
        return false;
    }
    finally{
        lukkSTM(stm);
        lukkForbindelse();
    }
    return true;
 }
 
//HENT FRA DATABASE
 
    public ArrayList<deltakerBean> hentDeltakere(){
        System.out.println("Henter deltakere fra DB");
        
        openDB();
        
        String qDeltakere = "SELECT fornavn, etternavn,adresse,epost,kjonn,fodselsaar FROM deltaker";
        Statement stmD = nySTM();
        ResultSet resD = nyRES(stmD, qDeltakere);
            
        ArrayList<deltakerBean> dbean = new ArrayList<deltakerBean>();
        
        try{
            while(resD.next()){
                dbean.add(
                    new deltakerBean(
                        resD.getString("fornavn"),
                        resD.getString("etternavn"),
                        resD.getString("adresse"),
                        resD.getString("epost"),
                        resD.getString("kjonn").charAt(0),
                        resD.getInt("fodselsaar")
                    )
                );
            }
        }
        catch(SQLException sqle){
            System.err.println("Kunne ikkje hente deltakere fra database:\n"+sqle);
            return null;
        }
        finally{
            lukkRSF(resD, stmD);
        }
        
        return dbean;
    }
    public ArrayList<String> hentKlasser(){
        System.out.println("Henter klasser fra DB");
        
        openDB();
        
        String qKlasser = "SELECT aldersinndeling, kjonn from klasse";
        Statement stmK = nySTM();
        ResultSet resK = nyRES(stmK, qKlasser);
        
        
        ArrayList<String> klasser = new ArrayList<String>();
        try{
            while(resK.next()){
                klasser.add(resK.getString("kjonn") +" "+resK.getString("aldersinndeling"));
            }
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje hente klasser fra Database:n"+se);
            return null;
        }
        finally{
            lukkRSF(resK, stmK);
        }
        
        return klasser;
    }
    public ArrayList<lopBean> hentLop(){
        System.out.println("Henter løp fra DB");
    
        openDB();
        
        String qLop = "SELECT navn, distanse, beskrivelse FROM lop";
        Statement stmL = nySTM();
        ResultSet resL = nyRES(stmL, qLop);

        ArrayList<lopBean> lb = new ArrayList<lopBean>();
        try{
            while(resL.next()){
                lb.add(
                    new lopBean(
                        resL.getString("navn"),
                        Float.parseFloat(resL.getString("distanse")),
                        resL.getString("beskrivelse")
                    ));
            }
        }
        catch(SQLException se){
            System.err.println("Kunne ikkje hente løp fra database:\n"+se);
            return null;
        }
        finally{
            lukkRSF(resL, stmL);
        }
        return lb;
    }
    public ArrayList<LopDeltaker> hentResultat(int lop, ArrayList<deltakerBean> d){
        ++lop;
        System.out.println("Henter resultater for løp_id "+lop+" fra DB");
        
        openDB();
        String qRes = "SELECT startnr, tid, deltaker_id, lop_id,klasse_id FROM resultat WHERE lop_id="+lop;
        Statement stm = nySTM();
        ResultSet rRes = nyRES(stm, qRes);
        
        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();
        try{
            while(rRes.next()){
                ld.add(new LopDeltaker(
                        rRes.getInt("startnr"),
                        d.get(rRes.getInt("deltaker_id")-1),
                        rRes.getInt("klasse_id"), 
                        rRes.getTime("tid"))
                        );
            }
            return ld;
        }
        catch(SQLException se){
            System.err.println("Feil med henting av Resultat: \n"+se);
            return null;
        }
        finally{
            lukkRSF(rRes,stm);
        }
        
    }
    public ArrayList<LopDeltaker> hentResultat(int lop, ArrayList<deltakerBean> d, String sortering){
        ++lop;
        System.out.println("Henter resultater for løp_id "+lop+" fra DB med sortering på "+sortering);
        
        openDB();
        String qRes = "SELECT startnr, tid, deltaker_id, lop_id,klasse_id FROM resultat WHERE lop_id="+lop+" ORDER BY "+ sortering;
        Statement stm = nySTM();
        ResultSet rRes = nyRES(stm, qRes);
        
        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();
        try{
            while(rRes.next()){
                ld.add(new LopDeltaker(
                        rRes.getInt("startnr"),
                        d.get(rRes.getInt("deltaker_id")-1),
                        rRes.getInt("klasse_id"), 
                        rRes.getTime("tid"))
                        );
            }
            return ld;
        }
        catch(SQLException se){
            System.err.println("Feil med henting av Resultat: \n"+se);
            return null;
        }
        finally{
            lukkRSF(rRes,stm);
        }
        
    }
    public Time hentSnittTid(int lop){
        ++lop;
        System.out.println("Henter snitttid for løp_id "+lop);
        
        openDB();
        String qRes = "select tid from resultat where lop_id="+lop+" and tid is not null";
        Statement stm = nySTM();
        ResultSet rRes = nyRES(stm, qRes);
        
        ArrayList<LopDeltaker> ld = new ArrayList<LopDeltaker>();
        try{
            long tid = 0;
            int antall =0;
            while(rRes.next()){
                tid += rRes.getTime("tid").getTime();
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
            lukkRSF(rRes,stm);
        }     
    }
    
//SKRIV TIL DATABASE
    
    public int skrivLop(lopBean lb){
        System.out.println("Skriver løp til DB: "+lb.getNavn());
        
        openDB();
        
        try{
            PreparedStatement ps = db.prepareStatement(
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
            lukkForbindelse();
        }
    }
    public int skrivDeltaker(deltakerBean d){
        System.out.println("Skriv deltaker til DB: "+d.getEpost());
        
        openDB();
        
        try{
            PreparedStatement ps = db.prepareStatement(
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
            lukkForbindelse();
        }
    }
    
    public int skrivDeltakerTilLop(int deltaker, int lop, int klasse, int startnr){
        System.out.println("Skriv deltaker til DB");
        
        openDB();
        
        try{
            PreparedStatement ps = db.prepareStatement(
                    "INSERT INTO resultat (startnr, deltaker_id, lop_id, klasse_id) "+
                    "VALUES (?,?,?,?)");
            ps.setInt(1, ++startnr);
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
            lukkForbindelse();
        }
    }
    public int skrivResultat(Time tid, int startnr, int lop){
        System.out.println("Skriv resultat til DB");
        
        openDB();

        try{
            PreparedStatement ps = db.prepareStatement(
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
            lukkForbindelse();
        }
    }
    
}    
