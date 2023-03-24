import java.io.Serializable;

/**
 * The Pay class represents a payment made to an employee. It contains various
 * types of pay such as normal pay, evening pay, night pay, extra pay, and
 * overtime pay. It also includes an overtime wage wich reprosents hourly wage
 * extra for overtime
 * This class implements Serializable to allow for object serialization.
 * 
 * @author Matti Voutilainen
 */
public class Pay implements Serializable {
    private double normalPay;
    private double eveningPay;
    private double nigthPay;
    private double extraPay;
    private double overtimePay;
    private double overtimeWage;

    /**
     * Constructs a new Pay object with all pay components set to 0.
     */
    public Pay() {
        this.normalPay = 0;
        this.eveningPay = 0;
        this.nigthPay = 0;
        this.extraPay = 0;
        this.overtimePay = 0;
    }

    /**
     * Constructs a new Pay object with the specified pay components.
     *
     * @param normalPay    the normal pay component
     * @param eveningPay   the evening pay component
     * @param nigthPay     the night pay component
     * @param extraPay     the extra pay component
     * @param overtimePay  the overtime pay component
     * @param overTimeWage the wage rate for overtime pay
     */
    public Pay(double normalPay, double eveningPay, double nigthPay, double extraPay, double overtimePay,
            double overTimeWage) {
        this.normalPay = normalPay;
        this.eveningPay = eveningPay;
        this.nigthPay = nigthPay;
        this.extraPay = extraPay;
        this.overtimePay = overtimePay;
        this.overtimeWage = overTimeWage;
    }

    /**
     * Returns the normal pay component.
     *
     * @return the normal pay component
     */
    public double getNormalPay() {
        return this.normalPay;
    }

    /**
     * Returns the evening pay component.
     *
     * @return the evening pay component
     */
    public double getEveningPay() {
        return this.eveningPay;
    }

    /**
     * Returns the night pay component.
     *
     * @return the night pay component
     */
    public double getNigthPay() {
        return this.nigthPay;
    }

    /**
     * Returns the extra pay component.
     *
     * @return the extra pay component
     */
    public double getExtraPay() {
        return this.extraPay;
    }

    /**
     * Returns the overtime pay component.
     *
     * @return the overtime pay component
     */
    public double getOvertimePay() {
        return this.overtimePay;
    }

    /**
     * Returns the wage rate for overtime pay.
     *
     * @return the wage rate for overtime pay
     */
    public double getOvertimeWage() {
        return this.overtimeWage;
    }

    /**
     * Returns the total pay amount, including all pay components.
     *
     * @return the total pay amount
     */
    public double getTotaPay() {
        return this.normalPay + this.eveningPay + this.nigthPay +
                this.extraPay + this.overtimePay;
    }

    /**
     * Adds the given amount to the normal pay amount.
     * 
     * @param normalPay the amount to add to normal pay
     */
    public void addNormalPay(double normalPay) {
        this.normalPay += normalPay;
    }

    /**
     * Adds the given amount to the evening pay amount.
     * 
     * @param eveningPay the amount to add to evening pay
     */
    public void addEveningPay(double eveningPay) {
        this.eveningPay += eveningPay;
    }

    /**
     * Adds the given amount to the night pay amount.
     * 
     * @param nightPay the amount to add to night pay
     */
    public void addNigthPay(double nigthPay) {
        this.nigthPay += nigthPay;
    }

    /**
     * Adds the given amount to the extra pay amount.
     * 
     * @param extraPay the amount to add to extra pay
     */
    public void addExtraPay(double extraPay) {
        this.extraPay += extraPay;
    }

    /**
     * Adds the given amount to the overtime pay amount.
     * 
     * @param overtimePay the amount to add to overtime pay
     */
    public void addOvertimePay(double overtimePay) {
        this.overtimePay += overtimePay;
    }

    /**
     * Adds the given amount to the overtime wage.
     * 
     * @param overtimeWage the amount to add to overtime wage
     */
    public void addOvertimeWage(double overtimeWage) {
        this.overtimeWage += overtimeWage;
    }
}