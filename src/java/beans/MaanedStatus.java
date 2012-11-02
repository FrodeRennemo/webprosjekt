package beans;

/**
 *
 * @author Frode
 */
public class MaanedStatus {

    private Treningsokt okten;

    public MaanedStatus(Treningsokt okten) {
        this.okten = okten;
    }

    public MaanedStatus() {
        okten = new Treningsokt();
    }

    public Treningsokt getOkten() {
        return okten;
    }

    public void setOkten(Treningsokt nyOkt) {
        okten = nyOkt;
    }
}
