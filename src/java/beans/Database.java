package beans;

import static javax.swing.JOptionPane.*;
import java.sql.*;

class Database {

    public static void main(String[] args) {
        String databasedriver = "org.apache.derby.jdbc.ClientDriver";
        Connection forbindelse = null;
        try { // feilutgang hvis databasetilkopling mislykkes
            Class.forName(databasedriver);
            String databasenavn ="jdbc:derby://localhost:1527/waplj_prosjekt;user=waplj;password=waplj";
            forbindelse = DriverManager.getConnection(databasenavn);
        } catch (Exception e) {
            System.out.println("Feil 1: " + e);
            System.exit(0);  // avslutter
        }
        Statement setning = null;
        ResultSet res = null;
        try {
            setning = forbindelse.createStatement();
            res = setning.executeQuery("select * from bruker");
            while (res.next()) {
                String brukernavn = res.getString("brukernavn");
                String passord = res.getString("passord");
                System.out.println(brukernavn + " " + passord);
            }
        } catch (SQLException e) {
            System.out.println("Feil 2: " + e);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                System.out.println("Feil 3: " + e);
            } finally {
                try {
                    if (setning != null) {
                        setning.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Feil 4: " + e);
                } finally {
                    try {
                        if (forbindelse != null) {
                            forbindelse.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("Feil 5:" + e);
                    }
                } // finally, lukking av Statement-objekt
            } // finally, lukking av ResultSet-objekt
        } // finally
    }
}
