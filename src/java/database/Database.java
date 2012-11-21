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
    private Connection connection;
    private String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

    public Database() {
        try {
            Context con = new InitialContext();
            ds = (DataSource) con.lookup("jdbc/personressurs");
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String getUser(){
        return user;
    }
    public boolean logIn(User user) {
        PreparedStatement sqlLogIn = null;
        openConnection();
        boolean ok = false;
        try {
            sqlLogIn = connection.prepareStatement("SELECT * FROM user WHERE userNAVN = '"+user.getUsername()+"' AND PASSORD = '"+user.getPassword()+"' ");
            ResultSet res = sqlLogIn.executeQuery();
            connection.commit();
            if(res.next()){
                 ok = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(sqlLogIn);
        }
        closeConnection();
        return ok;
    }
    
    public boolean endrePassord(User user) {
        PreparedStatement sqlLogIn = null;
        openConnection();
        boolean ok = false;
        try {
            sqlLogIn = connection.prepareStatement("UPDATE user SET PASSORD = ? WHERE userNAVN = ?");
            sqlLogIn.setString(1, user.getPassword());
            sqlLogIn.setString(2, user.getUsername());
            sqlLogIn.executeUpdate();
            connection.commit();
            ok = true;
         

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(sqlLogIn);
        }
        closeConnection();
        return ok;
    }




//    public boolean nyuser() {
//        PreparedStatement sqlRegNyuser = null;
//        openConnection();
//        boolean ok = false;
//        try {
//            sqlRegNyuser = connection.prepareStatement("insert into user(usernavn,passord) values(?, ?)");
//            sqlRegNyuser.setString(1, nyuser.getusernavn());
//            sqlRegNyuser.setString(2, nyuser.getPassord());
//
//
//            connection.commit();
//
//            /*
//             * Transaksjon slutt
//             */
//            ok = true;
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            Opprydder.rollback(connection);
//
//        } finally {
//            Opprydder.settAutoCommit(connection);
//            Opprydder.closeSentence(sqlRegNyuser);
//        }
//        closeConnection();
//        return ok;
//    }

    public ArrayList<Workout> readIn() {
        ArrayList<Workout> tab = new ArrayList<Workout>();
        PreparedStatement sqlLesInn = null;
        openConnection();

        try {
            sqlLesInn = connection.prepareStatement("SELECT * FROM TRENING WHERE usernavn = ?");
            sqlLesInn.setString(1, user);
            ResultSet res = sqlLesInn.executeQuery();
            while (res.next()) {
                Date date = res.getDate("date");
                int duration = res.getInt("duration");
                String category = res.getString("kategorinavn");
                String text = res.getString("tekst");
                int number = res.getInt("workoutnr");
                Workout workout = new Workout(date, duration, text, category);
                workout.setNummer(number);
                tab.add(workout);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return tab;
    }

    public ArrayList<Workout> lesInUser() {
        ArrayList<Workout> tab = new ArrayList<Workout>();
        PreparedStatement sqlLesInn = null;
        openConnection();
        try {
            //Statement setning = connection.createStatement();
            sqlLesInn = connection.prepareStatement("SELECT * FROM TRENING WHERE usernavn = ?");
            sqlLesInn.setString(1, user);
            ResultSet res = sqlLesInn.executeQuery();
            while (res.next()) {
                Date date = res.getDate("date");
                int duration = res.getInt("duration");
                String category = res.getString("categorynavn");
                String text = res.getString("tekst");
                Workout workout = new Workout(date, duration, text, category);
                tab.add(workout);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return tab;
    }

    private void openConnection() {
        try {
            if (ds == null) {
                throw new SQLException("Ingen connection");
            }
            connection = ds.getConnection();
            System.out.println("Databaseconnection opprettet");
        } catch (SQLException e) {
            Cleaner.writeMessage(e, "Konstruktøren");
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private void closeConnection() {
        System.out.println("Lukker databaseconnectionn");
        Cleaner.closeConnection(connection);
    }

    public boolean regNew(Workout nyworkout) {
        PreparedStatement sqlRegNew = null;
        openConnection();
        boolean ok = false;
        try {
            sqlRegNew = connection.prepareStatement("insert into trening(date,duration,categorynavn,tekst,usernavn) values(?, ?, ?,?,?)");
            try {
                sqlRegNew.setDate(1, new java.sql.Date(newWorkout.getdate().getTime()));
            } catch (NullPointerException e) {
                sqlRegNew.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            }
            sqlRegNew.setInt(2, nyworkout.getduration());
            sqlRegNew.setString(3, nyworkout.getcategory());
            sqlRegNew.setString(4, nyworkout.gettext());
            sqlRegNew.setString(5, user);
            sqlRegNew.executeUpdate();

            connection.commit();

            /*
             * Transaksjon slutt
             */
            ok = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(sqlRegNyworkout);
        }
        closeConnection();
        return ok;
    }

    public boolean deleteWorkout(Workout workout) {
        boolean ok = false;
        PreparedStatement sqlUpdWorkout = null;

        System.out.println(workout.getNummer());
        openConnection();
        try {
            sqlUpdWorkout = connection.prepareStatement("DELETE FROM TRENING WHERE number = ?");
            sqlUpdWorkout.setInt(1, workout.getNummer());
            sqlUpdWorkout.executeUpdate();
            connection.commit();
            ok = true;

        } catch (SQLException e) {
            Cleaner.writeMessage(e, "slettworkout()");
        } finally {
            Cleaner.closeSentence(sqlUpdWorkout);
        }
        closeConnection();
        return ok;



    }
//    public int finnnumber(Treningsworkout workout, String usernavn){
//        int primNokkel = -1;
//        try{
//        openConnection();
//      Statement setning = connection.createStatement();
//      Statement setning2 = connection.createStatement();
//      java.sql.Date jall = new java.sql.Date(workout.getdate().getTime());
//      ResultSet res2 = setning2.executeQuery("SELECT * FROM TRENING");
//      java.sql.Date date = new java.sql.Date(workout.getdate().getTime());
//      while(res2.next()){
//          if(res2.getDate(2).equals(date) && res2.getString(6).equals(usernavn)){
//              res2 = setning.executeQuery("DELETE FROM TRENING WHERE usernavn = '"+usernavn+"'");
//          }
//      }
//      
//      ResultSet res = setning.executeQuery("select number FROM TRENING WHERE userNAVN = '"+usernavn+"' AND date = ?" );
//      primNokkel = res.getInt(1);
//      closeConnection();
//        }catch(SQLException e){
//            System.out.println(e.getMessage());
//        }
//      return primNokkel;
//    }

    public boolean changeData(Workout workout) {
        PreparedStatement sqlUpdWorkout = null;
        boolean ok = false;
        openConnection();
        try {
            //String sql = "update eksemplar set laant_av = '" + navn + "' where isbn = '" + isbn + "' and eks_nr = " + eksNr;
            sqlUpdWorkout = connection.prepareStatement("update trening set date = ?,duration = ?,categorynavn = ?, tekst = ? where number = ? and usernavn = ?");
            try {
                sqlUpdWorkout.setDate(1, new java.sql.Date(workout.getDate().getTime()));
            } catch (NullPointerException e) {
                sqlUpdWorkout.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            }
            sqlUpdWorkout.setInt(2, workout.getDuration());
            sqlUpdWorkout.setString(3, workout.getDategory());
            sqlUpdWorkout.setString(4, workout.getText());
            sqlUpdWorkout.setInt(5, workout.getNumber());
            sqlUpdWorkout.setString(6, user);
            ok = true;
            sqlUpdWorkout.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Cleaner.writeMessage(e, "changeData()");
        } finally {
            Cleaner.closeSentence(sqlUpdWorkout);
        }
        closeConnection();
        return ok;
    }

    public static void main(String[] args) throws SQLException {
        /*
         * try { Class.forName("org.apache.derby.jdbc.ClientDriver"); } catch
         * (Exception e) { System.out.println("Kan ikke laste databasedriveren.
         * Avbryter."); e.printStackTrace(); System.exit(0); }
         */
        Database database = new Database();
        Workout workout = new Workout(new java.util.Date(), 45, "sdsd", "styrke");
    }
}
