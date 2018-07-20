package justinb99.sampleapi.engine.model;

import java.time.LocalTime;
import java.util.Set;

public class Rate {

  private Set<Day> days;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer price;

  public Set<Day> getDays() {
    return days;
  }

  public void setDays(Set<Day> days) {
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

}
