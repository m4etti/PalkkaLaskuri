package com.example;

import java.text.DecimalFormat;

import com.example.data.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * A custom GridPane used for displaying pay information.
 * 
 * @author Matti Voutilainen
 */
public class PayGrid extends GridPane {

    /**
     * Constructs a PayGrid with the given Pay, WorkHours, and tax.
     * 
     * @param pay   The Pay object containing pay information.
     * @param hours The WorkHours object containing work hour information.
     * @param tax   The tax rate as a decimal value.
     */
    public PayGrid(Pay pay, WorkHours hours, double tax) {
        double hourlyWage = pay.getNormalPay() / hours.getNormal();
        double extra = pay.getExtraPay() / hours.getNormal();
        TimeOfDayBonus eveningBonus = new TimeOfDayBonus(pay.getExtraPay() / hours.getNormal());
        TimeOfDayBonus nightBonus = new TimeOfDayBonus(pay.getExtraPay() / hours.getNormal());

        Settings settings = new Settings(hourlyWage, extra, eveningBonus, nightBonus, tax);

        buildGrid(pay, hours, settings);
    }

    /**
     * Constructs a PayGrid with the given WorkShift.
     * 
     * @param shift The WorkShift object containing work shift information such as
     *              work hours and pay.
     */
    public PayGrid(WorkShift shift) {
        WorkHours hours = shift.getWorkHours();
        Settings settings = shift.getSettings();
        Pay pay = shift.getPay();
        buildGrid(pay, hours, settings);
    }

    /**
     * Builds the PayGrid by adding Text objects to the GridPane with the
     * appropriate pay information.
     * 
     * @param pay      The Pay object containing pay information.
     * @param hours    The WorkHours object containing work hour information.
     * @param settings The Settings object containing wage and tax information.
     */
    private void buildGrid(Pay pay, WorkHours hours, Settings settings) {
        DecimalFormat df = new DecimalFormat("0.00");
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));

        add(new Text("Selitys"), 0, 0);
        add(new Text("Määrä"), 1, 0);
        add(new Text("A-hinta"), 2, 0);
        add(new Text("Summa"), 3, 0);

        add(new Text("Perus tunnit:"), 0, 1);
        add(new Text(df.format(hours.getNormal())), 1, 1);
        add(new Text(df.format(settings.getHourlyWage())), 2, 1);
        add(new Text(df.format(pay.getNormalPay())), 3, 1);

        add(new Text("Suorite lisä:"), 0, 2);
        add(new Text(df.format(hours.getNormal())), 1, 2);
        add(new Text(df.format(settings.getExtra())), 2, 2);
        add(new Text(df.format(pay.getExtraPay())), 3, 2);

        add(new Text("Ilta tunnit:"), 0, 3);
        add(new Text(df.format(hours.getEvening())), 1, 3);
        add(new Text(df.format(settings.getEavningBonus().getBonus())), 2, 3);
        add(new Text(df.format(pay.getEveningPay())), 3, 3);

        add(new Text("Yö tunnit:"), 0, 4);
        add(new Text(df.format(hours.getNight())), 1, 4);
        add(new Text(df.format(settings.getNightBonus().getBonus())), 2, 4);
        add(new Text(df.format(pay.getNightPay())), 3, 4);

        add(new Text("Ylityö tunni:"), 0, 5);
        add(new Text(df.format(hours.getOverTimeNormal())), 1, 5);
        add(new Text(df.format(pay.getOvertimeWage())), 2, 5);
        add(new Text(df.format(pay.getOvertimePay())), 3, 5);

        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);
        add(separator1, 0, 6, 4, 1);

        add(new Text("Yhteensä"), 0, 7);
        add(new Text(df.format(pay.getTotaPay())), 3, 7);

        add(new Text("Vero"), 0, 8);
        add(new Text(df.format(settings.getTax() * 100)), 2, 8);
        add(new Text(df.format(-pay.getTotaPay() * settings.getTax())), 3, 8);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.HORIZONTAL);
        add(separator2, 0, 9, 4, 1);

        add(new Text("Yhteensä"), 0, 10);
        add(new Text(df.format(pay.getTotaPay() - pay.getTotaPay() * settings.getTax())), 3, 10);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.HORIZONTAL);
        add(separator3, 0, 11, 4, 1);
    }

}
