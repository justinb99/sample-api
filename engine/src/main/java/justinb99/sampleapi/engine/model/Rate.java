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
  public boolean isAvailable(DateTimeRange dateTimeRange) {
    return dateTimeRange.getDayOfWeek()
      .filter(days::contains)
      .filter(dow -> dateTimeRange.getStartTime().compareTo(startTime) >= 0)
      .filter(dow -> dateTimeRange.getEndTime().compareTo(endTime) <= 0)
      .isPresent();
  }
}
