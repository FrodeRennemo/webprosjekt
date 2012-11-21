package workout;

/**
 *
 * @author Frode
 */
public class MonthStatus {

    private Workout okten;

    public MonthStatus(Workout okten) {
        this.okten = okten;
    }

    public MonthStatus() {
        okten = new Workout();
    }

    public Workout getOkten() {
        return okten;
    }

    public void setOkten(Workout nyOkt) {
        okten = nyOkt;
    }
}
