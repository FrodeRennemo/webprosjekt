package database;

import java.sql.*;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import user.User;
import workout.Workout;

public class Database {

    @Resource(name = "jdbc/personressurs")
    private DataSource ds;
    private Connection forbindelse;
    private String bruker = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

    public Database() {
        try {
            Context con = new InitialContext();
            ds = (DataSource) con.lookup("jdbc/personressurs");
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String getBruker(){
        return bruker;
    }
    public boolean loggInn(User bruker) {
        PreparedStatement sqlLoggInn = null;
        åpneForbindelse();
        boolean ok = false;
        try {
            sqlLoggInn = forbindelse.prepareStatement("SELECT * FROM BRUKER WHERE BRUKERNAVN = '"+bruker.getBrukernavn()+"' AND PASSORD = '"+bruker.getPassord()+"' ");
            ResultSet res = sqlLoggInn.executeQuery();
            forbindelse.commit();
            if(res.next()){
                 ok = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rullTilbake(forbindelse);

        } finally {
            Cleaner.settAutoCommit(forbindelse);
            Cleaner.lukkSetning(sqlLoggInn);
        }
        lukkForbindelse();
        return ok;
    }
    
    public boolean endrePassord(User bruker) {
        PreparedStatement sqlLoggInn = null;
        åpneForbindelse();
        boolean ok = false;
        try {
            sqlLoggInn = forbindelse.prepareStatement("UPDATE BRUKER SET PASSORD = ? WHERE BRUKERNAVN = ?");
            sqlLoggInn.setString(1, bruker.getPassord());
            sqlLoggInn.setString(2, bruker.getBrukernavn());
            sqlLoggInn.executeUpdate();
            forbindelse.commit();
            ok = true;
         

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rullTilbake(forbindelse);

        } finally {
            Cleaner.settAutoCommit(forbindelse);
            Cleaner.lukkSetning(sqlLoggInn);
        }
        lukkForbindelse();
        return ok;
    }




//    public boolean nyBruker() {
//        PreparedStatement sqlRegNyBruker = null;
//        åpneForbindelse();
//        boolean ok = false;
//        try {
//            sqlRegNyBruker = forbindelse.prepareStatement("insert into bruker(brukernavn,passord) values(?, ?)");
//            sqlRegNyBruker.setString(1, nyBruker.getBrukernavn());
//            sqlRegNyBruker.setString(2, nyBruker.getPassord());
//
//
//            forbindelse.commit();
//
//            /*
//             * Transaksjon slutt
//             */
//            ok = true;
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            Opprydder.rullTilbake(forbindelse);
//
//        } finally {
//            Opprydder.settAutoCommit(forbindelse);
//            Opprydder.lukkSetning(sqlRegNyBruker);
//        }
//        lukkForbindelse();
//        return ok;
//    }

    public ArrayList<Workout> lesInn() {
        ArrayList<Workout> tab = new ArrayList<Workout>();
        PreparedStatement sqlLesInn = null;
        åpneForbindelse();

        try {
            sqlLesInn = forbindelse.prepareStatement("SELECT * FROM TRENING WHERE brukernavn = ?");
            sqlLesInn.setString(1, bruker);
            ResultSet res = sqlLesInn.executeQuery();
            while (res.next()) {
                Date dato = res.getDate("dato");
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String beskrivelse = res.getString("tekst");
                int oktNr = res.getInt("oktnr");
                Workout okt = new Workout(dato, varighet, beskrivelse, kategori);
                okt.setNummer(oktNr);
                tab.add(okt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        lukkForbindelse();
        return tab;
    }

    public ArrayList<Workout> lesInnBruker() {
        ArrayList<Workout> tab = new ArrayList<Workout>();
        PreparedStatement sqlLesInn = null;
        åpneForbindelse();
        try {
            //Statement setning = forbindelse.createStatement();
            sqlLesInn = forbindelse.prepareStatement("SELECT * FROM TRENING WHERE brukernavn = ?");
            sqlLesInn.setString(1, bruker);
            ResultSet res = sqlLesInn.executeQuery();
            while (res.next()) {
                Date dato = res.getDate("dato");
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String beskrivelse = res.getString("tekst");
                Workout okt = new Workout(dato, varighet, beskrivelse, kategori);
                tab.add(okt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        lukkForbindelse();
        return tab;
    }

    private void åpneForbindelse() {
        try {
            if (ds == null) {
                throw new SQLException("Ingen forbindelse");
            }
            forbindelse = ds.getConnection();
            System.out.println("Databaseforbindelse opprettet");
        } catch (SQLException e) {
            Cleaner.skrivMelding(e, "Konstruktøren");
        }
    }

    public void setForbindelse(Connection forbindelse) {
        this.forbindelse = forbindelse;
    }

    public Connection getForbindelse() {
        return forbindelse;
    }

    private void lukkForbindelse() {
        System.out.println("Lukker databaseforbindelsen");
        Cleaner.lukkForbindelse(forbindelse);
    }

    public boolean regNyOkt(Workout nyOkt) {
        PreparedStatement sqlRegNyOkt = null;
        åpneForbindelse();
        boolean ok = false;
        try {
            sqlRegNyOkt = forbindelse.prepareStatement("insert into trening(dato,varighet,kategorinavn,tekst,brukernavn) values(?, ?, ?,?,?)");
            try {
                sqlRegNyOkt.setDate(1, new java.sql.Date(nyOkt.getDato().getTime()));
            } catch (NullPointerException e) {
                sqlRegNyOkt.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            }
            sqlRegNyOkt.setInt(2, nyOkt.getVarighet());
            sqlRegNyOkt.setString(3, nyOkt.getKategori());
            sqlRegNyOkt.setString(4, nyOkt.getBeskrivelse());
            sqlRegNyOkt.setString(5, bruker);
            sqlRegNyOkt.executeUpdate();

            forbindelse.commit();

            /*
             * Transaksjon slutt
             */
            ok = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rullTilbake(forbindelse);

        } finally {
            Cleaner.settAutoCommit(forbindelse);
            Cleaner.lukkSetning(sqlRegNyOkt);
        }
        lukkForbindelse();
        return ok;
    }

    public boolean slettOkt(Workout okt) {
        boolean ok = false;
        PreparedStatement sqlUpdOkt = null;

        System.out.println(okt.getNummer());
        åpneForbindelse();
        try {
            sqlUpdOkt = forbindelse.prepareStatement("DELETE FROM TRENING WHERE oktnr = ?");
            sqlUpdOkt.setInt(1, okt.getNummer());
            System.out.println(okt.getNummer());
            sqlUpdOkt.executeUpdate();
            forbindelse.commit();
            ok = true;

        } catch (SQLException e) {
            Cleaner.skrivMelding(e, "slettOkt()");
        } finally {
            Cleaner.lukkSetning(sqlUpdOkt);
        }
        lukkForbindelse();
        return ok;



    }
//    public int finnOktNr(Treningsokt okt, String brukernavn){
//        int primNokkel = -1;
//        try{
//        åpneForbindelse();
//      Statement setning = forbindelse.createStatement();
//      Statement setning2 = forbindelse.createStatement();
//      java.sql.Date jall = new java.sql.Date(okt.getDato().getTime());
//      ResultSet res2 = setning2.executeQuery("SELECT * FROM TRENING");
//      java.sql.Date dato = new java.sql.Date(okt.getDato().getTime());
//      while(res2.next()){
//          if(res2.getDate(2).equals(dato) && res2.getString(6).equals(brukernavn)){
//              res2 = setning.executeQuery("DELETE FROM TRENING WHERE brukernavn = '"+brukernavn+"'");
//          }
//      }
//      
//      ResultSet res = setning.executeQuery("select OKTNR FROM TRENING WHERE BRUKERNAVN = '"+brukernavn+"' AND DATO = ?" );
//      primNokkel = res.getInt(1);
//      lukkForbindelse();
//        }catch(SQLException e){
//            System.out.println(e.getMessage());
//        }
//      return primNokkel;
//    }

    public boolean endreData(Workout okt) {
        PreparedStatement sqlUpdOkt = null;
        boolean ok = false;
        åpneForbindelse();
        try {
            //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
            sqlUpdOkt = forbindelse.prepareStatement("update trening set dato = ?,varighet = ?,kategorinavn = ?, tekst = ? where oktnr = ? and brukernavn = ?");
            try {
                sqlUpdOkt.setDate(1, new java.sql.Date(okt.getDato().getTime()));
            } catch (NullPointerException e) {
                sqlUpdOkt.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            }
            sqlUpdOkt.setInt(2, okt.getVarighet());
            sqlUpdOkt.setString(3, okt.getKategori());
            sqlUpdOkt.setString(4, okt.getBeskrivelse());
            sqlUpdOkt.setInt(5, okt.getNummer());
            sqlUpdOkt.setString(6, bruker);
            ok = true;
            sqlUpdOkt.executeUpdate();
            forbindelse.commit();
        } catch (SQLException e) {
            Cleaner.skrivMelding(e, "endreData()");
        } finally {
            Cleaner.lukkSetning(sqlUpdOkt);
        }
        lukkForbindelse();
        return ok;
    }

    public static void main(String[] args) throws SQLException {
        /*
         * try { Class.forName("org.apache.derby.jdbc.ClientDriver"); } catch
         * (Exception e) { System.out.println("Kan ikke laste databasedriveren.
         * Avbryter."); e.printStackTrace(); System.exit(0); }
         */
        Database database = new Database();
        Workout okt = new Workout(new java.util.Date(), 45, "sdsd", "styrke");
    }
}
