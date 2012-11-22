package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private int year;
    private boolean sortDateAsc = true;
    private boolean sortDurationAsc = true;
    private boolean sortCategoryAsc = true;

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

    public synchronized List<WorkoutStatus> getTableData() {
        return tabledata;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
        List<WorkoutStatus> monthdata = Collections.synchronizedList(new ArrayList<WorkoutStatus>());
        for (Workout i : workouts.getWorkoutsMonth(month, year)) {
            monthdata.add(new WorkoutStatus(i));
        }
        tabledata = monthdata;
    }

    public synchronized void everyWorkout() {
        List<WorkoutStatus> everydata = Collections.synchronizedList(new ArrayList<WorkoutStatus>());
        if (workouts.getList() != null) {
            for (int i = 0; i < workouts.getList().size(); i++) {
                everydata.add(new WorkoutStatus(workouts.getList().get(i)));
            }
        }
        tabledata = everydata;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "../index.xhtml?faces-redirect=true";
    }

    public String sortByDate() {
        if (sortDateAsc) {
            //ascending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {

                    return w1.getDate().compareTo(w2.getDate());
                }
            });
            sortDateAsc = false;

        } else {
            //descending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {
                    return w2.getDate().compareTo(w1.getDate());
                }
            });
            sortDateAsc = true;
        }
        return null;
    }

    public String sortByDuration() {
        if (sortDurationAsc) {
            //ascending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {
                    if (w1.getDuration() == w2.getDuration()) {
                        return 0;
                    } else if (w1.getDuration() > w2.getDuration()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            sortDurationAsc = false;
        } else {
            //descending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {
                    if (w2.getDuration() == w1.getDuration()) {
                        return 0;
                    } else if (w2.getDuration() > w1.getDuration()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            sortDurationAsc = true;
        }
        return null;
    }

    public String sortByCategory() {
        if (sortCategoryAsc) {
            //ascending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {
                    return w1.getCategory().compareTo(w2.getCategory());
                }
            });
            sortCategoryAsc = false;
        } else {
            //descending order
            Collections.sort(tabledata, new Comparator<WorkoutStatus>() {

                @Override
                public int compare(WorkoutStatus w1, WorkoutStatus w2) {
                    return w2.getCategory().compareTo(w1.getCategory());
                }
            });
            sortCategoryAsc = true;
        }
        return null;
    }
}
