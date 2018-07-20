package justinb99.sampleapi.engine.model;

import java.time.DayOfWeek;

public enum Day {
  sun(DayOfWeek.SUNDAY),
  mon(DayOfWeek.MONDAY),
  tues(DayOfWeek.TUESDAY),
  wed(DayOfWeek.WEDNESDAY),
  thurs(DayOfWeek.THURSDAY),
  fri(DayOfWeek.FRIDAY),
  sat(DayOfWeek.SATURDAY);

  private DayOfWeek dayOfWeek;

  Day(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

}
