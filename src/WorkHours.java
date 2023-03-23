import java.io.Serializable;

public class WorkHours implements Serializable{
    private double normal;
    private double evening;
    private double nigth;
    private double overTimeNormal;
    private double overTimeEvening;
    private double overTimeNigth;

    public WorkHours() {
        this.normal = 0;
        this.evening = 0;
        this.nigth = 0;
        this.overTimeNormal = 0;
    }

    public WorkHours(double normal, double evening, double nigth, double overTimeNormal, double overTimeEvening, double overTimeNigth) {
        this.normal = normal;
        this.evening = evening;
        this.nigth = nigth;
        this.overTimeNormal = overTimeNormal;
        this.overTimeEvening = overTimeEvening;
        this.overTimeNigth = overTimeNigth;
    }

    // Double Getters

    public double getNormal() {
        return this.normal;
    }

    public double getEvening() {
        return this.evening;
    }

    public double getNigth() {
        return this.nigth;
    }

    public double getOverTimeNormal() {
        return this.overTimeNormal;
    }

    public double getOverTimeEvening() {
        return this.overTimeEvening;
    }

    public double getOverTimeNigth() {
        return this.overTimeNigth;
    }

    public void addNormal(double normal) {
        this.normal += normal;
    }

    public void addEvening(double evening) {
        this.evening += evening;
    }

    public void addNigth(double nigth) {
        this.nigth += nigth;
    }

    public void addOverTimeNormal(double overTimeNormal) {
        this.overTimeNormal += overTimeNormal;
    }

    
}
