/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package workout;

import database.Database;
import java.io.Serializable;
import java.util.ArrayList;

public class Workouts implements Serializable {
    private ArrayList<Workout> list = new ArrayList();
    private Database database = new Database(); 

    public Workouts() {
        this.list = database.readIn();
    }

    public Database getDatabase() {
        return database;
    }
    

    public void readIn() {
        database.readIn();
    }

    public ArrayList<Workout> getList() {
        return list;
    }

    public boolean registerNewWorkout(Workout workout) {
        if (workout != null) {
            if (database.regNew(workout)) {
                list.add(workout);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Workout> getWorkoutsMonth(int month, int year) {
        ArrayList<Workout> Month = new ArrayList<Workout>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDate() != null) {
                if (list.get(i).getDate().getMonth() + 1 == month && list.get(i).getDate().getYear() + 1900 == year) {
                    Month.add(list.get(i));
                }
            }
        }
        return Month;
    }
    
    

    public boolean deleteWorkout(Workout delWorkout) {
        if (database.deleteWorkout(delWorkout)) {
            list.remove(delWorkout);
            return true;
        }
        return false;
    }

    public int getAverageDuration() {
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getDuration();
        }
        if (list.isEmpty()) {
            return 0;
        }
        return (total / list.size());
    }

    public void changeData(Workout workout) {
        
        for(int i = 0;i<list.size();i++){
            if(workout.getNumber() == list.get(i).getNumber() && database.changeData(workout)){
                System.out.println(workout.getText());
                list.set(i, workout);
            }
        }
    }

    public String getUser() {
        return database.getUser();
    }

    @Override
    public String toString() {
        String utskrift = "";
        for (int i = 0; i < list.size(); i++) {
            utskrift += list.get(i).getDate() + " " + list.get(i).getDuration() + " " + list.get(i).getCategory() + " " + list.get(i).getText() + "\n";
        }
        return utskrift;
    }

}
