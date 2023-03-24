import java.io.Serializable;

/**
 * The Settings class represents the configuration settings for an employee's
 * wages and bonuses.
 * These settings include the hourly wage, extra amount, evening and night
 * bonuses, and tax percentage.
 * This class is serializable, meaning it can be converted into a stream of
 * bytes for storage or transmission.
 * 
 * @author Matti Voutilainen
 */
public class Settings implements Serializable {
    private double hourlyWage;
    private double extra;
    private TimeOfDayBonus eveningBonus;
    private TimeOfDayBonus nightBonus;
    private double tax;

    /**
     * Constructs a new Settings object with the given hourly wage, extra amount,
     * evening and night bonuses, and tax percentage.
     * 
     * @param hourlyWage   The hourly wage of the employee.
     * @param extra        The extra amount added to the employee's wages.
     * @param eveningBonus The evening bonus for the employee's wages.
     * @param nightBonus   The night bonus for the employee's wages.
     * @param tax          The tax percentage applied to the employee's wages.
     */
    public Settings(double hourlyWage, double extra, TimeOfDayBonus eavningBonus, TimeOfDayBonus nightBonus,
            double tax) {
        this.hourlyWage = hourlyWage;
        this.extra = extra;
        this.eveningBonus = eavningBonus;
        this.nightBonus = nightBonus;
        this.tax = tax;
    }

    /**
     * Sets all of the settings for the employee's wages and bonuses.
     * 
     * @param hourlyWage   The hourly wage of the employee.
     * @param extra        The extra amount added to the employee's wages.
     * @param eveningBonus The evening bonus for the employee's wages.
     * @param nightBonus   The night bonus for the employee's wages.
     * @param tax          The tax percentage applied to the employee's wages.
     */
    public void setAll(double hourlyWage, double extra, TimeOfDayBonus eavningBonus, TimeOfDayBonus nightBonus,
            double tax) {
        this.hourlyWage = hourlyWage;
        this.extra = extra;
        this.eveningBonus = eavningBonus;
        this.nightBonus = nightBonus;
        this.tax = tax;
    }

    /**
     * Returns the hourly wage of the employee.
     * 
     * @return The hourly wage of the employee.
     */
    public double getHourlyWage() {
        return this.hourlyWage;
    }

    /**
     * Returns the extra amount added to the employee's wages.
     * 
     * @return The extra amount added to the employee's wages.
     */
    public double getExtra() {
        return this.extra;
    }

    /**
     * Returns the evening bonus for the employee's wages.
     * 
     * @return The evening bonus for the employee's wages.
     */
    public TimeOfDayBonus getEavningBonus() {
        return this.eveningBonus;
    }

    /**
     * Returns the night bonus for the employee's wages.
     * 
     * @return The night bonus for the employee's wages.
     */
    public TimeOfDayBonus getNightBonus() {
        return this.nightBonus;
    }

    /**
     * Returns the tax percentage applied to the employee's wages.
     * 
     * @return The tax percentage applied to the employee's wages.
     */
    public double getTax() {
        return this.tax;
    }
}
