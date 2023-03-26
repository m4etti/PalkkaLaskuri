package com.example.data;

import java.io.Serializable;

/**
 * The WorkHours class represents the number of hours worked by an employee,
 * broken down into different categories such as normal hours, evening hours,
 * and overtime hours.
 * 
 * This class provides methods to get and set the different categories of work
 * hours, and to
 * add additional hours to each category. It also implements the Serializable
 * interface, so
 * objects of this class can be easily serialized and deserialized for storage
 * or transport.
 * 
 * @author Matti Voutilainen
 */
public class WorkHours implements Serializable {
    /**
     * The number of normal hours worked.
     */
    private double normal;
    /**
     * The number of evening hours worked.
     */
    private double evening;
    /**
     * The number of night hours worked.
     */
    private double night;
    /**
     * The number of overtime hours worked during normal hours.
     */
    private double overTimeNormal;
    /**
     * The number of overtime hours worked during evening hours.
     */
    private double overTimeEvening;
    /**
     * The number of overtime hours worked during night hours.
     */
    private double overTimeNight;

    /**
     * Constructs a new WorkHours object with all values initialized to 0.
     */
    public WorkHours() {
        this.normal = 0;
        this.evening = 0;
        this.night = 0;
        this.overTimeNormal = 0;
    }

    /**
     * Constructs a new WorkHours object with the specified values.
     *
     * @param normal          the number of normal hours worked
     * @param evening         the number of evening hours worked
     * @param night           the number of night hours worked
     * @param overTimeNormal  the number of overtime hours worked during normal
     *                        hours
     * @param overTimeEvening the number of overtime hours worked during evening
     *                        hours
     * @param overTimeNight   the number of overtime hours worked during night hours
     * 
     */
    public WorkHours(double normal, double evening, double night, double overTimeNormal, double overTimeEvening,
            double overTimeNight) {
        this.normal = normal;
        this.evening = evening;
        this.night = night;
        this.overTimeNormal = overTimeNormal;
        this.overTimeEvening = overTimeEvening;
        this.overTimeNight = overTimeNight;
    }

    /**
     * Returns the number of normal hours worked.
     *
     * @return the number of normal hours worked
     */
    public double getNormal() {
        return this.normal;
    }

    /**
     * Returns the number of evening hours worked.
     *
     * @return the number of evening hours worked
     */
    public double getEvening() {
        return this.evening;
    }

    /**
     * Returns the number of night hours worked.
     *
     * @return the number of night hours worked
     */
    public double getNight() {
        return this.night;
    }

    /**
     * Returns the number of overtime hours worked during all hours.
     *
     * @return the number of overtime hours worked during all hours
     */
    public double getOverTimeNormal() {
        return this.overTimeNormal;
    }

    /**
     * Returns the number of overtime hours worked during evening hours.
     *
     * @return the number of overtime hours worked during evening hours
     */
    public double getOverTimeEvening() {
        return this.overTimeEvening;
    }

    /**
     * Returns the number of overtime hours worked during night hours.
     *
     * @return the number of overtime hours worked during night hours
     */
    public double getOverTimeNight() {
        return this.overTimeNight;
    }

    /**
     * Adds the specified value to the number of normal hours worked.
     *
     * @param normal the value to add to the number of normal hours worked
     */
    public void addNormal(double normal) {
        this.normal += normal;
    }

    /**
     * Adds the specified value to the number of evening hours worked.
     *
     * @param evening the value to add to the number of evening hours worked
     */
    public void addEvening(double evening) {
        this.evening += evening;
    }

    /**
     * Adds the specified value to the number of nigth hours worked.
     * 
     * @param nigth the value to add to the number of nigth hours worked
     */
    public void addNigth(double nigth) {
        this.night += nigth;
    }

    /**
     * Adds the specified value to the number of overtime hours worked.
     * 
     * @param overTimeNormal the value to add to the number of overtime hours worked
     */
    public void addOverTimeNormal(double overTimeNormal) {
        this.overTimeNormal += overTimeNormal;
    }
}
