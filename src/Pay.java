import java.io.Serializable;

public class Pay implements Serializable{
    private double normalPay;
    private double eveningPay;
    private double nigthPay;
    private double extraPay;
    private double overtimePay;
    private double overtimeWage;

    public Pay() {
        this.normalPay = 0;
        this.eveningPay = 0;
        this.nigthPay = 0;
        this.extraPay = 0;
        this.overtimePay = 0;
    }

    public Pay(double normalPay, double eveningPay, double nigthPay, double extraPay, double overtimePay, double overTimeWage) {
        this.normalPay = normalPay;
        this.eveningPay = eveningPay;
        this.nigthPay = nigthPay;
        this.extraPay = extraPay;
        this.overtimePay = overtimePay;
        this.overtimeWage = overTimeWage;
    }

    // Double getters

    public double getNormalPay() {
        return this.normalPay;
    }

    public double getEveningPay() {
        return this.eveningPay;
    }

    public double getNigthPay() {
        return this.nigthPay;
    }

    public double getExtraPay() {
        return this.extraPay;
    }

    public double getOvertimePay() {
        return this.overtimePay;
    }

    public double getOvertimeWage() {
        return this.overtimeWage;
    }

    public double getTotaPay(){
        return this.normalPay + this.eveningPay + this.nigthPay + 
        this.extraPay + this.overtimePay;
    }

    public void addNormalPay(double normalPay) {
        this.normalPay += normalPay;
    }

    public void addEveningPay(double eveningPay) {
        this.eveningPay += eveningPay;
    }

    public void addNigthPay(double nigthPay) {
        this.nigthPay += nigthPay;
    }

    public void addExtraPay(double extraPay) {
        this.extraPay += extraPay;
    }

    public void addOvertimePay(double overtimePay) {
        this.overtimePay += overtimePay;
    }

    public void addOvertimeWage(double overtimeWage) {
        this.overtimeWage += overtimeWage;
    }


}