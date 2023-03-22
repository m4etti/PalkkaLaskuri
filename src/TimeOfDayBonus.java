import java.io.Serializable;
import java.time.LocalTime;

public class TimeOfDayBonus implements Serializable {
    private double bonus;
    private LocalTime start;
    private LocalTime end;

    public TimeOfDayBonus(double bonus, LocalTime start, LocalTime end) {
        this.bonus = bonus;
        this.start = start;
        this.end = end;
    }

    public double getBonus() {
        return this.bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public LocalTime getStart() {
        return this.start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return this.end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

}