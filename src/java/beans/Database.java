package beans;

import static javax.swing.JOptionPane.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date.*;

class Database {

    private String dbNavn;
    private Connection forbindelse;

    public Database(String startDbNavn) {
        dbNavn = startDbNavn;
    }

    public ArrayList<Treningsokt> lesInn()  {

        
        ArrayList<Treningsokt> tab = new ArrayList<Treningsokt>();
        
        åpneForbindelse();
        try{
        Statement setning = forbindelse.createStatement();
        // ResultSet res = setning.executeQuery("Select * FROM TRENING WHERE brukernavn= '"+brukernavn+"'");
        ResultSet res = setning.executeQuery("Select * FROM TRENING");
        
        while (res.next()) {
            Date dato = res.getDate("dato");
            int varighet = res.getInt("varighet");
            String kategori = res.getString("kategorinavn");
            String beskrivelse = res.getString("tekst");
            Treningsokt okt = new Treningsokt(dato, varighet, beskrivelse, kategori);
            tab.add(okt);
        }
        }catch(SQLException e){
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

            forbindelse.setAutoCommit(false);

            sqlRegNyOkt = forbindelse.prepareStatement("insert into trening(dato,varighet,kategorinavn,tekst,brukernavn) values(?, ?, ?,?,?)");
            try{
                sqlRegNyOkt.setDate(1, new java.sql.Date(nyOkt.getDato().getTime()));
            }catch(NullPointerException e){
                sqlRegNyOkt.setDate(1, new java.sql.Date(2012,11,2));
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
            Opprydder.rullTilbake(forbindelse);
            String sqlStatus = e.getSQLState().trim();
            String statusklasse = sqlStatus.substring(0, 2);
            if (statusklasse.equals("23")) { // standard kode for "integrity constraint violation"
                ok = false;  // bok med denne isbn er registrert fra før
            } else {
                Opprydder.skrivMelding(e, "regNyOkt()");
            }
        } finally {
            Opprydder.settAutoCommit(forbindelse);
            Opprydder.lukkSetning(sqlRegNyOkt);
        }
        lukkForbindelse();
        return ok;
    }

    public boolean slettOkt(int indeks,String brukernavn) {
    
         boolean ok = false;
        PreparedStatement sqlUpdOkt = null;
        åpneForbindelse();
        try {
            //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
            sqlUpdOkt = forbindelse.prepareStatement("DELETE FROM TRENING WHERE oktnr = ?");
            sqlUpdOkt.setInt(1, indeks);
           
            int ant = sqlUpdOkt.executeUpdate();
            if (ant > 0) {
                ok = true;
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "slettOkt()");
        } finally {
            Opprydder.lukkSetning(sqlUpdOkt);
        }
        lukkForbindelse();
        return ok;
    


    }

    public boolean endreData(String brukernavn, int oktNr, Date dato, int varighet, String beskrivelse, String kategori) {

        boolean ok = false;
        PreparedStatement sqlUpdOkt = null;
        åpneForbindelse();
        try {
            //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
            sqlUpdOkt = forbindelse.prepareStatement("update trening set dato = ?,varighet = ?,kategorinavn = ?, tekst = ? where oktnr = ? and brukernavn = ?");
            sqlUpdOkt.setDate(1, new java.sql.Date(dato.getTime()));
            sqlUpdOkt.setInt(2, varighet);
            sqlUpdOkt.setString(3, kategori);
            sqlUpdOkt.setString(4, beskrivelse);
            sqlUpdOkt.setInt(5, oktNr);
            sqlUpdOkt.setString(6, brukernavn);
            int ant = sqlUpdOkt.executeUpdate();
            if (ant > 0) {
                ok = true;
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "lånUtEksemplar()");
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
        boolean[] ok = new boolean[4];
        ok[0] = database.endreData("anne", 3, new java.sql.Date(new java.util.Date().getTime()), 60, null, "aerobics");
        System.out.println(ok[0] + ", " + ok[1] + ", " + ok[2] + ", " + ok[3]); //utskrift: true, true, (exception) false, true

    }
}
