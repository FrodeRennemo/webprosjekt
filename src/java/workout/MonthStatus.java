package workout;

public class MonthStatus {

    private Workout workout;

    public MonthStatus(Workout workout) {
        this.workout = workout;
    }

    public MonthStatus() {
        workout = new Workout();
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout newWork) {
        workout = newWork;
    }
}
