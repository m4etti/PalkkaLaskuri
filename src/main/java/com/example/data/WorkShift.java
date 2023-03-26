package com.example.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The WorkShift class represents a work shift with a start time, end time,
 * settings, work hours, and pay.
 * It provides methods for retrieving the start time, end time, settings, work
 * hours, and pay, as well
 * as modifying the start time, end time, and settings of a work shift. The
 * class also calculates the
 * work hours and pay of a work shift based on its start time, end time, and
 * settings.
 * WorkShift objects are serializable.
 * 
 * @author Matti Voutilainen
 */
public class WorkShift implements Serializable {
  /**
   * The start time of the work shift.
   */
  private LocalDateTime start;
  /**
   * The end time of the work shift.
   */
  private LocalDateTime end;
  /**
   * The settings for the work shift. Settings contain wage and tax information
   */
  private Settings settings;
  /**
   * The work hours for the work shift.
   */
  private WorkHours workHours;
  /**
   * The pay for the work shift.
   */
  private Pay pay;

  /**
   * Constructs a new WorkShift object with the specified start time, end time,
   * and settings.
   * Calculates the work hours and pay for the work shift based on the start time,
   * end time, and settings.
   * 
   * @param start    the start time of the work shift.
   * @param end      the end time of the work shift.
   * @param settings the settings for the work shift.
   */
  public WorkShift(LocalDateTime start, LocalDateTime end, Settings settings) {
    this.start = start;
    this.end = end;
    this.settings = settings;
    this.workHours = calculateHours();
    this.pay = calculatePay();
  }

  /**
   * Returns the start time of the work shift.
   * 
   * @return the start time of the work shift
   */
  public LocalDateTime getStart() {
    return this.start;
  }

  /**
   * Returns the end time of the work shift.
   * 
   * @return the end time of the work shift.
   */
  public LocalDateTime getEnd() {
    return this.end;
  }

  /**
   * Returns the settings for the work shift.
   * 
   * @return the settings for the work shift.
   */
  public Settings getSettings() {
    return settings;
  }

  /**
   * Modifies the start time, end time, and settings of the work shift.
   * Calculates the work hours and pay for the work shift based on the modified
   * start time, end time, and settings.
   * 
   * @param start    the new start time of the work shift.
   * @param end      the new end time of the work shift.
   * @param settings the new settings for the work shift.
   */
  public void modify(LocalDateTime start, LocalDateTime end, Settings settings) {
    this.start = start;
    this.end = end;
    this.settings = settings;
    this.workHours = calculateHours();
    this.pay = calculatePay();
  }

  /**
   * Returns the work hours for the work shift.
   * 
   * @return the work hours for the work shift.
   */
  public WorkHours getWorkHours() {
    return this.workHours;
  }

  /**
   * Returns the pay for the work shift.
   * 
   * @return the pay for the work shift.
   */
  public Pay getPay() {
    return this.pay;
  }

  /**
   * Calculates the work hours for the work shift based on its start time, end
   * time, and settings.
   * 
   * @return The work hours for the work shift.
   */
  private WorkHours calculateHours() {
    // Create 2 versions of evening and night times to avoid crossing over the day.
    LocalDateTime[] eveningTimes = modifyPeriodTo2Days(this.start.toLocalDate(), settings.getEavningBonus().getStart(),
        settings.getEavningBonus().getEnd());
    LocalDateTime[] nigthTimes = modifyPeriodTo2Days(this.start.toLocalDate(), settings.getNightBonus().getStart(),
        settings.getNightBonus().getEnd());

    double normalDuration = 0;
    double eveningDuration = 0;
    double nightDuration = 0;
    double overtimeEveningDuration = 0;
    double overtimeNightDuration = 0;
    double overtimeNormalDuration = 0;
    // Calculate duration of normal, evening, and night hours, as well as overtime

    // when the shift includes evening bonus
    eveningDuration = overlapDuration(this.start, this.end, eveningTimes[0], eveningTimes[1]);
    eveningDuration += overlapDuration(this.start, this.end, eveningTimes[2], eveningTimes[3]);

    // when the shift includes night bonus
    nightDuration = overlapDuration(this.start, this.end, nigthTimes[0], nigthTimes[1]);
    nightDuration += overlapDuration(this.start, this.end, nigthTimes[2], nigthTimes[3]);

    // Calculate the total duration of the shift
    normalDuration = Duration.between(this.start, this.end).toHours();

    // Calculate overtime hours
    if (normalDuration > 480) {
      LocalDateTime overtimeStart = this.start.plusHours(8);

      // when overtime includes evening bonus
      eveningDuration = overlapDuration(overtimeStart, this.end, eveningTimes[0], eveningTimes[1]);
      eveningDuration += overlapDuration(overtimeStart, this.end, eveningTimes[2], eveningTimes[3]);

      // when overtime includes night bonus
      overtimeEveningDuration = overlapDuration(overtimeStart, this.end, nigthTimes[0], nigthTimes[1]);
      overtimeEveningDuration += overlapDuration(overtimeStart, this.end, nigthTimes[2], nigthTimes[3]);

      // Calculate total overtime hours
      overtimeNormalDuration = Duration.between(overtimeStart, this.end).toHours();
    }

    // Return an object with calculated hours
    return new WorkHours(normalDuration, eveningDuration, nightDuration, overtimeNormalDuration,
        overtimeEveningDuration, overtimeNightDuration);
  }

  /**
   * Returns an array of LocalDateTime instances that represent the start and end
   * times for a period of two days, starting from the given today's date and the
   * given start and end times.
   * If the end time is before the start time, the period will overlap between two
   * different days.
   * 
   * @param today the date representing the start day of the period
   * @param start the time representing the start time of the period
   * @param end   the time representing the end time of the period
   * @return an array of LocalDateTime instances representing the start and end
   *         times of the period.
   */
  private LocalDateTime[] modifyPeriodTo2Days(LocalDate today, LocalTime start, LocalTime end) {
    LocalDate tomorrow = today.plusDays(1);
    LocalDateTime newStart1;
    LocalDateTime newEnd1;
    LocalDateTime newStart2;
    LocalDateTime newEnd2;

    if (end.isBefore(start)) {
      newStart1 = LocalDateTime.of(today, LocalTime.MIDNIGHT);
      newEnd1 = LocalDateTime.of(today, end);

      newStart2 = LocalDateTime.of(today, start);
      newEnd2 = LocalDateTime.of(tomorrow, end);
    } else {
      newStart1 = LocalDateTime.of(today, start);
      newEnd1 = LocalDateTime.of(today, end);

      newStart2 = LocalDateTime.of(tomorrow, start);
      newEnd2 = LocalDateTime.of(tomorrow, end);
    }
    LocalDateTime[] out = { newStart1, newEnd1, newStart2, newEnd2 };

    return out;
  }

  /**
   * Returns the duration in hours of the overlap between two periods of time
   * specified
   * by their start and end times.
   * 
   * @param start1 the start time of the first period
   * @param end1   the end time of the first period
   * @param start2 the start time of the second period
   * @param end2   the end time of the second period
   * @return the duration of the overlap between the two periods in hours
   */
  private double overlapDuration(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
    LocalDateTime overlapStart = start1.isAfter(start2) ? start1 : start2;
    LocalDateTime overlapEnd = end1.isBefore(end2) ? end1 : end2;
    Duration overlapDuration = Duration.between(overlapStart, overlapEnd);
    if (overlapDuration.isNegative()) {
      return 0;
    } else {
      return overlapDuration.toHours();
    }

  }

  /**
   * Calculates and returns the pay for the worked hours based on the hourly wage,
   * evening bonus,
   * night bonus, extra pay, and overtime pay. If there is no overtime, the
   * overtime wage is zero.
   * 
   * @return a Pay object representing the calculated pay for the worked hours
   */
  private Pay calculatePay() {
    // Calculate regular pay and bonuses
    double normalPay = workHours.getNormal() * (settings.getHourlyWage());
    double eveningPay = workHours.getEvening() * (settings.getEavningBonus().getBonus());
    double nigthPay = workHours.getNight() * (settings.getNightBonus().getBonus());
    double extraPay = workHours.getNormal() * (settings.getExtra());

    // Calculate overtime pay
    double overtimeNormalPay = workHours.getOverTimeNormal() * (settings.getHourlyWage() * 0.5);
    double overtimeEveningPay = workHours.getOverTimeEvening() * settings.getEavningBonus().getBonus();
    double overtimeNigthPay = workHours.getOverTimeNight() * settings.getNightBonus().getBonus();
    double overtimePay = overtimeNormalPay + overtimeEveningPay + overtimeNigthPay;
    double overtimeWage = 0;
    if (workHours.getOverTimeNormal() != 0) {
      overtimeWage = overtimePay / workHours.getOverTimeNormal();

    }
    return new Pay(normalPay, eveningPay, nigthPay, extraPay, overtimePay, overtimeWage);

  }
}