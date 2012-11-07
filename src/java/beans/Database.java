package beans;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date.*;

class Database {

    private String dbNavn;
    private Connection forbindelse;

    public Database(String startDbNavn) {
        dbNavn = startDbNavn;
    }

    public ArrayList<Treningsokt> lesInn() {
        ArrayList<Treningsokt> tab = new ArrayList<Treningsokt>();
        åpneForbindelse();
        try {
            Statement setning = forbindelse.createStatement();
            ResultSet res = setning.executeQuery("Select * FROM TRENING");
            while (res.next()) {
                int oktnummer = res.getInt("oktnr");
                Date dato = res.getDate("dato");
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String beskrivelse = res.getString("tekst");
                Treningsokt okt = new Treningsokt(dato, varighet, beskrivelse, kategori);
                okt.setNummer(oktnummer);
                tab.add(okt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        lukkForbindelse();
        return tab;
    }

    public ArrayList<Treningsokt> lesInnBruker(String bruker) {
        ArrayList<Treningsokt> tab = new ArrayList<Treningsokt>();
        åpneForbindelse();
        try {
            Statement setning = forbindelse.createStatement();
            ResultSet res = setning.executeQuery("Select * FROM TRENING WHERE bruker = '" + bruker + "'");
            while (res.next()) {
                int oktNr = res.getInt("oktnr");
                Date dato = res.getDate("dato");
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String beskrivelse = res.getString("tekst");
                Treningsokt okt = new Treningsokt(dato, varighet, beskrivelse, kategori);
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
            forbindelse = DriverManager.getConnection(dbNavn);
            System.out.println("Databaseforbindelse opprettet");
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "Konstruktøren");
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    private void lukkForbindelse() {
        System.out.println("Lukker databaseforbindelsen");
        Opprydder.lukkForbindelse(forbindelse);
    }

    public boolean regNyOkt(Treningsokt nyOkt, String brukernavn) {
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
            sqlRegNyOkt.setString(5, brukernavn);
            sqlRegNyOkt.executeUpdate();

            forbindelse.commit();

            /*
             * Transaksjon slutt
             */
            ok = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Opprydder.rullTilbake(forbindelse);

        } finally {
            Opprydder.settAutoCommit(forbindelse);
            Opprydder.lukkSetning(sqlRegNyOkt);
        }
        lukkForbindelse();
        return ok;
    }

    public boolean slettOkt(Treningsokt okt, String brukernavn) {
        boolean ok = false;
        PreparedStatement sqlUpdOkt = null;
        Statement setning = null;
        System.out.println(okt.getNummer());
        åpneForbindelse();
        try {
            setning = forbindelse.createStatement();
            setning.executeUpdate("DELETE FROM TRENING WHERE oktnr = " + okt.getNummer() + "");
            //sqlUpdOkt = forbindelse.prepareStatement("DELETE FROM TRENING WHERE oktnr = " + okt.getNummer() + "");
            System.out.println(okt.getNummer());
            ok = true;

        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "slettOkt()");
        } finally {
            Opprydder.lukkSetning(setning);
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

    public boolean endreData(Treningsokt okt, String brukernavn) {
        PreparedStatement sqlUpdOkt = null;
        boolean ok = false;
        åpneForbindelse();
        try {
            //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
            sqlUpdOkt = forbindelse.prepareStatement("update trening set dato = ?,varighet = ?,kategorinavn = ?, tekst = ? where oktnr = ? and brukernavn = ?");
            sqlUpdOkt.setDate(1, new java.sql.Date(okt.getDato().getTime()));
            sqlUpdOkt.setInt(2, okt.getVarighet());
            sqlUpdOkt.setString(3, okt.getKategori());
            sqlUpdOkt.setString(4, okt.getBeskrivelse());
            sqlUpdOkt.setInt(5, okt.getNummer());
            sqlUpdOkt.setString(6, brukernavn);
            ok = true;
            sqlUpdOkt.executeUpdate();
            forbindelse.commit();
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "endreData()");
        } finally {
            Opprydder.lukkSetning(sqlUpdOkt);
        }
        lukkForbindelse();
        return ok;
    }

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (Exception e) {
            System.out.println("Kan ikke laste databasedriveren. Avbryter.");
            e.printStackTrace();
            System.exit(0);
        }

        String databasenavn = "jdbc:derby://localhost:1527/waplj_prosjekt;user=waplj;password=waplj";
        Database database = new Database(databasenavn);
        Treningsokt okt = new Treningsokt(new java.util.Date(), 45, "sdsd", "styrke");
        database.regNyOkt(okt, "tore");
//        System.out.println(database.finnOktNr(okt, "tore"));
    }
}
