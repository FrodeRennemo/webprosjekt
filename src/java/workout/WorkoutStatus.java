
package workout;

import java.util.Date;

public class WorkoutStatus {

    private Workout workout;
    private boolean delete;
    private boolean change;

    public WorkoutStatus(Workout workout) {
        this.workout = workout;
        delete = false;
        change = false;
    }

    public WorkoutStatus() {
        workout = new Workout();
        delete = false;
        change = false;
    }

    public synchronized boolean getDelete() {
        return delete;
    }

    public void setDelete(boolean newDelete) {
        delete = newDelete;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout newWork) {
        workout = newWork;
    }

    public synchronized boolean getChange() {
        return change;
    }

    public void setChange(boolean changes) {
        change = changes;
    }
    
    public Date getDate(){
        return workout.getDate();
    }
    
    public void setDate(Date date){
        workout.setDate(date);
    }

    public void change() {
        if (!change) {
            change = true;
        }
    }
}
