package justinb99.sampleapi.engine.model;

import java.time.*;
import java.util.Set;

public class Rate {

  private Set<DayOfWeek> days;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer price;

  public Set<DayOfWeek> getDays() {
    return days;
  }

  public void setDays(Set<DayOfWeek> days) {
    this.days = days;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  //Per spec, A rate must completely encapsulate a datetime range for it to be available.
  //Rates will never overlap.
  //assumes start and end are both UTC
  public boolean isAvailable(OffsetDateTime start, OffsetDateTime end) {
//    start.getDayOfWeek();
    //If days.contains(start.getDayOfWeek())
//      if (getStartTime() <= start.toLocalTime() && end.toLocalTime() <= getEndTime())
    //return true;
    return false;
  }
}
