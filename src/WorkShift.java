import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class WorkShift {
  private LocalDateTime start;
  private LocalDateTime end;
  private double hourlyWage;
  private double extra;
  private TimeOfDayBonus eavningBonus;
  private TimeOfDayBonus nightBonus;

  public WorkShift(LocalDateTime start, LocalDateTime end, double hourlyWage, double extra, TimeOfDayBonus eavningBonus,
      TimeOfDayBonus nightBonus) {
    this.start = start;
    this.end = end;
    this.hourlyWage = hourlyWage;
    this.extra = extra;
    this.eavningBonus = eavningBonus;
    this.nightBonus = nightBonus;
  }

  public WorkShift(LocalDateTime start){
    this.start = start;
  }

  public WorkShift(LocalDateTime start, LocalDateTime end){
    this.start = start;
    this.end = end;
  }

  public LocalDateTime getStart() {
    return this.start;
  }

  public LocalDateTime getEnd() {
    return this.end;
  }

  public void setTimes(LocalDateTime start, LocalDateTime end){
    this.start = start;
    this.end = end;
  }

  public Pay getPay() {
    return calculatePay();
  }

  private Pay calculatePay() {
    LocalTime startTime = this.start.toLocalTime();
    LocalTime endTime = this.end.toLocalTime();
    double eveningDuration = 0;
    double nightDuration = 0;
    double overtimeEveningDuration = 0;
    double overtimeNightDuration = 0;
    double overtimePay = 0;
    // lasketaan ilta ja yö lisän aikaiset jaksot ja niiden pituus

    // työvuoro sisältää iltalisää
    if (endTime.isAfter(eavningBonus.getStart()) && eavningBonus.getEnd().isAfter(startTime)) {
      eveningDuration = overlapDuration(startTime, endTime, eavningBonus.getStart(), eavningBonus.getEnd());
    }
    // työvuoro sisältää yölisää
    if (endTime.isAfter(nightBonus.getStart()) && nightBonus.getEnd().isAfter(startTime)) {
      nightDuration = overlapDuration(startTime, endTime, nightBonus.getStart(), nightBonus.getEnd());
    }
    // koko vuoron pituus
    double normalDuration = Duration.between(startTime, endTime).toMinutes();

    // lasketaan palkat ja lisät
    double normalPay = normalDuration * (this.hourlyWage) / 60.0;
    double eveningPay = eveningDuration * (eavningBonus.getBonus()) / 60.0;
    double nigthPay = nightDuration * (nightBonus.getBonus()) / 60.0;
    double extraPay = normalDuration * (this.extra) / 60.0;

    // lasketaan tunti ylityöt
    if (normalDuration > 480) {
      LocalTime overtimeStart = startTime.plusHours(8);
      // ylityö sisältää iltalisää
      if (endTime.isAfter(eavningBonus.getStart()) && eavningBonus.getEnd().isAfter(overtimeStart)) {
        overtimeEveningDuration = overlapDuration(overtimeStart, endTime, eavningBonus.getStart(),
            eavningBonus.getEnd());
      }
      // ylityö sisältää yölisää
      if (endTime.isAfter(nightBonus.getStart()) && nightBonus.getEnd().isAfter(overtimeStart)) {
        overtimeNightDuration = overlapDuration(overtimeStart, endTime, nightBonus.getStart(), nightBonus.getEnd());
      }
      // koko ylityön pituus
      double overtimeNormalDuration = Duration.between(overtimeStart, endTime).toMinutes();

      // ylityö palkat
      double overtimeNormalPay = overtimeNormalDuration * (this.hourlyWage * 0.5) / 60.0;
      double overtimeEveningPay = overtimeEveningDuration * (eavningBonus.getBonus() * 0.5) / 60.0;
      double overtimeNigthPay = overtimeNightDuration * (nightBonus.getBonus() * 0.5) / 60.0;
      overtimePay = overtimeNormalPay + overtimeEveningPay + overtimeNigthPay;

    }
    return new Pay(normalPay, eveningPay, nigthPay, extraPay, overtimePay);

  }

  // laske aikojen leikkaus
  private double overlapDuration(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
    LocalTime overlapStart = start1.isAfter(start2) ? start1 : start2;
    LocalTime overlapEnd = end1.isBefore(end2) ? end1 : end2;
    return Duration.between(overlapStart, overlapEnd).toMinutes();
  }
}