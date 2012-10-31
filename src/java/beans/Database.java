package beans;

import static javax.swing.JOptionPane.*;
import java.sql.*;
import java.util.Date.*;

class Database {
  private String dbNavn;
  private Connection forbindelse;

  /*
   * Hvis dette skal være en bean-klasse, må det eksistere en konstruktør med tom parameterliste.
   * Da må databasenavnet settes på annen måte, f.eks. hardkodes i linje 46.
   */
  public Database(String startDbNavn) {
    dbNavn = startDbNavn;
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

  /*
   * Metode som registrerer en ny tittel og 1.eksemplar av denne tittelen.
   * Metoden returnerer false dersom tittel med denne ISBN er registrert fra før.
   */
  public  boolean regNyOkt(Treningsokt nyOkt,String brukernavn) {
    System.out.println("regNyOkt(): " + nyOkt);
    PreparedStatement sqlRegNyOkt = null;
    åpneForbindelse();
    boolean ok = false;
    try {
     
      forbindelse.setAutoCommit(false);

      sqlRegNyOkt = forbindelse.prepareStatement("insert into trening values(?, ?, ?,?,?)");

      /* Oppdaterer boktabellen */
      sqlRegNyOkt.setDate(1, new java.sql.Date(nyOkt.getDato().getTime()));
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
      } else Opprydder.skrivMelding(e, "regNyBok()");
    } finally {
      Opprydder.settAutoCommit(forbindelse);
      Opprydder.lukkSetning(sqlRegNyOkt);
    }
    lukkForbindelse();
    return ok;
  }



  public boolean endreData(int oktNr,Date dato,int varighet, String beskrivelse, String kategori) {

    boolean ok = false;
    PreparedStatement sqlUpdOkt = null;
    åpneForbindelse();
    try {
      //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
      sqlUpdOkt = forbindelse.prepareStatement("update trening set dato = ?,varighet = ?,kategorinavn = ?, tekst = ? where oktnr = ?");
      sqlUpdOkt.setDate(1, new java.sql.Date(dato.getTime()));
      sqlUpdOkt.setInt(2, varighet);
      sqlUpdOkt.setString(3, kategori);
      sqlUpdOkt.setString(4, beskrivelse);
      sqlUpdOkt.setInt(5, oktNr);
      int ant = sqlUpdOkt.executeUpdate();
      if (ant > 0) ok = true;
    } catch (SQLException e) {
      Opprydder.skrivMelding(e, "lånUtEksemplar()");
    } finally {
      Opprydder.lukkSetning(sqlUpdOkt);
    }
    lukkForbindelse();
    return ok;
  }
}
/*
class BibliotekForWeb {
  public static void main(String[] args) throws Exception {
    try {
      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (Exception e) {
      System.out.println("Kan ikke laste databasedriveren. Avbryter.");
      e.printStackTrace();
      System.exit(0);
    }

    String databasenavn = "jdbc:derby://localhost:1527/ov5_vprg;user=vprg;password=vprg";

    Database database = new Database(databasenavn);

    /* Følgende er kun for testformål, forenklet frigjøring av databaseressurser */
/*
    Connection forbindelse = null;
    try {
      forbindelse = DriverManager.getConnection(databasenavn);
      Statement st = forbindelse.createStatement();
      int ant1 = st.executeUpdate("delete from eksemplar");
      st.close();
      st = forbindelse.createStatement();
      int ant2 = st.executeUpdate("delete from boktittel");
      st.close();
      System.out.println("Har slettet " + ant1 + " rader fra eksemplar og " + ant2 + " rader fra boktittel");
      forbindelse.close();

    } catch (SQLException e) {
      System.out.println("Problemer med å tømme databasen. Avslutter.");
      e.printStackTrace();
      forbindelse.close();
      System.exit(0);
    }

    /* Her begynner testingen */
/*
    boolean[] ok = new boolean[4];
    ok[0] = database.regNyBok(new Bok("0-201-50998-X", "The Unified Modeling Language Reference Manual", "J. Rumbaugh, I. Jacobson, G. Booch"));
    ok[1] = database.regNyBok(new Bok("0-07-241163-5", "C++ Program Design", "J. P. Cohoon, J. W. Davidson"));
    ok[2] = database.regNyBok(new Bok("0-07-241163-5", "C++ Program Design", "J. P. Cohoon, J. W. Davidson"));
    ok[3] = database.regNyBok(new Bok("0-596-00123-1", "Bulding Java Enterprise Applications", "Brett Mclaughlin"));
    System.out.println(ok[0] + ", " + ok[1] + ", " + ok[2] + ", " + ok[3]); //utskrift: true, true, (exception) false, true

    int[] eksnr = new int[4];
    eksnr[0] = database.regNyttEksemplar("0-201-50998-X");  // retur 2
    eksnr[1] = database.regNyttEksemplar("0-201-50998-X");  // retur 3
    eksnr[2] = database.regNyttEksemplar("0-07-241163-5");  // retur 2
    eksnr[3] = database.regNyttEksemplar("0-201-50968-X");  // retur 0 (ugyldig isbn)
    System.out.println(eksnr[0] + ", " + eksnr[1] + ", " + eksnr[2] + ", " + eksnr[3]); //utskrift: 2, 3, 2, 0

    ok[0] = database.lånUtEksemplar("0-201-50998-X", "Anne Hansen", 3);
    ok[1] = database.lånUtEksemplar("0-201-50968-X", "Per Hansen", 2); // ugyldig isbn
    ok[2] = database.lånUtEksemplar("0-596-00123-1", "Inge Hansen", 3); // ugyldig eksnr
    ok[3] = database.lånUtEksemplar("0-596-00123-1", "Inge Hansen", 1);
    System.out.println(ok[0] + ", " + ok[1] + ", " + ok[2] + ", " + ok[3]); //utskrift: true, false, false, true
  }
}

/* Utskrift:

Databaseforbindelse opprettet
Har slettet 6 rader fra eksemplar og 3 rader fra boktittel
regNyBok(): 0-201-50998-X: J. Rumbaugh, I. Jacobson, G. Booch, The Unified Model
ing Language Reference Manual
regNyBok(): 0-07-241163-5: J. P. Cohoon, J. W. Davidson, C++ Program Design
regNyBok(): 0-07-241163-5: J. P. Cohoon, J. W. Davidson, C++ Program Design
regNyBok(): 0-596-00123-1: Brett Mclaughlin, Bulding Java Enterprise Applications
true, true, false, true
regNyttEksemplar(), isbn = 0-201-50998-X
regNyttEksemplar(), isbn = 0-201-50998-X
regNyttEksemplar(), isbn = 0-07-241163-5
regNyttEksemplar(), isbn = 0-201-50968-X
2, 3, 2, 0
lånUtEksemplar(), isbn = 0-201-50998-X, navn = Anne Hansen, eksnr = 3
lånUtEksemplar(), isbn = 0-201-50968-X, navn = Per Hansen, eksnr = 2
lånUtEksemplar(), isbn = 0-596-00123-1, navn = Inge Hansen, eksnr = 3
lånUtEksemplar(), isbn = 0-596-00123-1, navn = Inge Hansen, eksnr = 1
true, false, false, true
Lukker databaseforbindelsen
*/

