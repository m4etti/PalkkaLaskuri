package com.example.data;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * A class representing a bonus given during a specific time of day.
 * This class implements Serializable to allow for object serialization.
 * 
 * @author Matti Voutilainen
 */
public class TimeOfDayBonus implements Serializable {
    /**
     * The bonus amount.
     */
    private double bonus;
    /**
     * The start time of the bonus period.
     */
    private LocalTime start;
    /**
     * The end time of the bonus period.
     */
    private LocalTime end;

    /**
     * Constructor for creating a bonus with no specific time of day.
     *
     * @param bonus the bonus amount.
     */
    public TimeOfDayBonus(double bonus) {
        this.bonus = bonus;
    }

    /**
     * Constructor for creating a bonus with a specific time of day.
     *
     * @param bonus The bonus amount.
     * @param start The start time of the bonus period.
     * @param end   The end time of the bonus period.
     */
    public TimeOfDayBonus(double bonus, LocalTime start, LocalTime end) {
        this.bonus = bonus;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the bonus amount.
     *
     * @return the bonus amount.
     */
    public double getBonus() {
        return this.bonus;
    }

    /**
     * Sets the bonus amount.
     *
     * @param bonus the bonus amount.
     */
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    /**
     * Returns the start time of the bonus period.
     *
     * @return the start time of the bonus period.
     */
    public LocalTime getStart() {
        return this.start;
    }

    /**
     * Sets the start time of the bonus period.
     *
     * @param start the start time of the bonus period.
     */
    public void setStart(LocalTime start) {
        this.start = start;
    }

    /**
     * Returns the end time of the bonus period.
     *
     * @return the end time of the bonus period.
     */
    public LocalTime getEnd() {
        return this.end;
    }

    /**
     * Sets the end time of the bonus period.
     *
     * @param end the end time of the bonus period.
     */
    public void setEnd(LocalTime end) {
        this.end = end;
    }

}