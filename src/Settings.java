import java.io.Serializable;

public class Settings implements Serializable {
    private double hourlyWage;
    private double extra;
    private TimeOfDayBonus eveningBonus;
    private TimeOfDayBonus nightBonus;

    public Settings(double hourlyWage, double extra, TimeOfDayBonus eavningBonus, TimeOfDayBonus nightBonus) {
        this.hourlyWage = hourlyWage;
        this.extra = extra;
        this.eveningBonus = eavningBonus;
        this.nightBonus = nightBonus;
    }

    public Settings() {
    }

    public double getHourlyWage() {
        return this.hourlyWage;
    }

    public double getExtra() {
        return this.extra;
    }

    public TimeOfDayBonus getEavningBonus() {
        return this.eveningBonus;
    }

    public TimeOfDayBonus getNightBonus() {
        return this.nightBonus;
    }

    public void setAll(double hourlyWage, double extra, TimeOfDayBonus eavningBonus, TimeOfDayBonus nightBonus) {
        this.hourlyWage = hourlyWage;
        this.extra = extra;
        this.eveningBonus = eavningBonus;
        this.nightBonus = nightBonus;
    }
}
