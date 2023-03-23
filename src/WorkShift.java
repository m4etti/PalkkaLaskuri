import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WorkShift implements Serializable {
  private LocalDateTime start;
  private LocalDateTime end;
  private Settings settings;
  private WorkHours workHours;
  private Pay pay;

  public WorkShift(LocalDateTime start, LocalDateTime end, Settings settings) {
    this.start = start;
    this.end = end;
    this.settings = settings;
    this.workHours = calculateHours();
    this.pay = calculatePay();
  }

  public LocalDateTime getStart() {
    return this.start;
  }

  public LocalDateTime getEnd() {
    return this.end;
  }

  public Settings getSettings() {
    return settings;
  }

  public void modify(LocalDateTime start, LocalDateTime end, Settings settings) {
    this.start = start;
    this.end = end;
    this.settings = settings;
    this.workHours = calculateHours();
    this.pay = calculatePay();
  }

  public WorkHours getWorkHours() {
    return this.workHours;
  }

  public Pay getPay() {
    return this.pay;
  }

  private WorkHours calculateHours() {
    // Luodaan ilta ja yö ajoille 2 versiota ettei vuorokauden ylitys sotke laskuja.
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
    // lasketaan ilta ja yö lisän aikaiset jaksot ja niiden pituus

    // työvuoro sisältää iltalisää
    eveningDuration = overlapDuration(this.start, this.end, eveningTimes[0], eveningTimes[1]);
    eveningDuration += overlapDuration(this.start, this.end, eveningTimes[2], eveningTimes[3]);

    // työvuoro sisältää yölisää
    nightDuration = overlapDuration(this.start, this.end, nigthTimes[0], nigthTimes[1]);
    nightDuration += overlapDuration(this.start, this.end, nigthTimes[2], nigthTimes[3]);

    // koko vuoron pituus
    normalDuration = Duration.between(this.start, this.end).toHours();

    // lasketaan tunti ylityöt
    if (normalDuration > 480) {
      LocalDateTime overtimeStart = this.start.plusHours(8);
      // ylityö sisältää iltalisää
      eveningDuration = overlapDuration(overtimeStart, this.end, eveningTimes[0], eveningTimes[1]);
      eveningDuration += overlapDuration(overtimeStart, this.end, eveningTimes[2], eveningTimes[3]);

      // ylityö sisältää yölisää
      overtimeEveningDuration = overlapDuration(overtimeStart, this.end, nigthTimes[0], nigthTimes[1]);
      overtimeEveningDuration += overlapDuration(overtimeStart, this.end, nigthTimes[2], nigthTimes[3]);

      // koko ylityön pituus
      overtimeNormalDuration = Duration.between(overtimeStart, this.end).toHours();
    }

    return new WorkHours(normalDuration, eveningDuration, nightDuration, overtimeNormalDuration,
        overtimeEveningDuration, overtimeNightDuration);
  }

  // jaa aikaväli kahdelle päivälle
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

  // laske aikojen leikkaus
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

  private Pay calculatePay() {
    // lasketaan palkat ja lisät
    double normalPay = workHours.getNormal() * (settings.getHourlyWage());
    double eveningPay = workHours.getEvening() * (settings.getEavningBonus().getBonus());
    double nigthPay = workHours.getNigth() * (settings.getNightBonus().getBonus());
    double extraPay = workHours.getNormal() * (settings.getExtra());

    // ylityö palkat
    double overtimeNormalPay = workHours.getOverTimeNormal() * (settings.getHourlyWage() * 0.5);
    double overtimeEveningPay = workHours.getOverTimeEvening() * settings.getEavningBonus().getBonus();
    double overtimeNigthPay = workHours.getOverTimeNigth() * settings.getNightBonus().getBonus();
    double overtimePay = overtimeNormalPay + overtimeEveningPay + overtimeNigthPay;
    double overtimeWage = 0;
    if (workHours.getOverTimeNormal() != 0) {
      overtimeWage = overtimePay / workHours.getOverTimeNormal();

    }
    return new Pay(normalPay, eveningPay, nigthPay, extraPay, overtimePay, overtimeWage);

  }
}