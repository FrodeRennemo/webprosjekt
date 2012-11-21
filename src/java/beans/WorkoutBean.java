package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import workout.MonthStatus;
import workout.Workout;
import workout.WorkoutStatus;
import workout.Workouts;

@Named
@SessionScoped
public class WorkoutBean implements java.io.Serializable {

    private Workouts workouts = new Workouts();
    private List<WorkoutStatus> tabledata = Collections.synchronizedList(new ArrayList<WorkoutStatus>());
    private Workout tempWorkout = new Workout(); // midlertidig lager for ny transaksjon
    private int month;
    private List<MonthStatus> monthdata = Collections.synchronizedList(new ArrayList<MonthStatus>());

    public WorkoutBean() {
        if (workouts.getList() != null) {
            for (int i = 0; i < workouts.getList().size(); i++) {
                tabledata.add(new WorkoutStatus(workouts.getList().get(i)));
            }
        }
    }

    public synchronized boolean getDataExist() {
        return (tabledata.size() > 0);
    }

    public Workouts getWorkouts() {
        return workouts;
    }

    public synchronized List<MonthStatus> getMonthdata() {
        return monthdata;
    }

    public synchronized boolean getDataMonthExist() {
        return (monthdata.size() > 0);
    }

    public synchronized List<WorkoutStatus> getTableData() {
        return tabledata;
    }

    public synchronized void setMonth(int month) {
        this.month = month;
    }

    public synchronized int getMonth() {
        return month;
    }

    public synchronized Date getDate() {
        return tempWorkout.getDate();
    }

    public synchronized void setDate(Date nyDato) {
        tempWorkout.setDate(nyDato);
    }

    public synchronized String getCategory() {
        return tempWorkout.getCategory();
    }

    public synchronized void setCategory(String category) {
        tempWorkout.setCategory(category);
    }

    public synchronized String getText() {
        return tempWorkout.getText();
    }

    public synchronized void setText(String text) {
        tempWorkout.setText(text);
    }

    public synchronized int getDuration() {
        return tempWorkout.getDuration();
    }

    public synchronized void setDuration(int duration) {
        tempWorkout.setDuration(duration);
    }

    public synchronized Workout getTempWorkout() {
        return tempWorkout;
    }

    public synchronized void setTempWorkout(Workout nyOkt) {
        tempWorkout = nyOkt;
    }

    public synchronized void add() {
        Workout nyOkt = new Workout(tempWorkout.getDate(), tempWorkout.getDuration(), tempWorkout.getText(), tempWorkout.getCategory());
        if (workouts.registerNewWorkout(nyOkt)) {
            tabledata.add(new WorkoutStatus(nyOkt));
            if (nyOkt.getDate() == null) {
                nyOkt.setDate(new java.sql.Date(new java.util.Date().getTime()));
            }
            tempWorkout.reset();
        }
    }

    public synchronized void delete() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            WorkoutStatus ts = tabledata.get(index);
            if (ts.getDelete() && workouts.deleteWorkout(ts.getWorkout())) {
                tabledata.remove(index);
            }
            index--;
        }
    }

    public synchronized double getAverageDuration() {
        return workouts.getAverageDuration();
    }

    public synchronized void workoutsMonth() {
        monthdata = Collections.synchronizedList(new ArrayList<MonthStatus>());
        for (Workout i : workouts.getWorkoutsMonth(month)) {
            monthdata.add(new MonthStatus(i));
        }
    }

    public synchronized void change() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            WorkoutStatus ts = tabledata.get(index);
            if (ts.getDate() == null) {
                ts.setDate(new java.sql.Date(new java.util.Date().getTime()));
            }
            ts.setChange(false);
            workouts.changeData(ts.getWorkout());
            index--;
        }
    }

    public String logout() {
        // FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "../index.xhtml?faces-redirect=true";
    }
}
