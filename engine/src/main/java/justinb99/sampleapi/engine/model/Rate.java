package justinb99.sampleapi.engine.model;

import justinb99.sampleapi.schema.RateOuterClass;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

public class Rate {

  private Set<DayOfWeek> days;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer price;
  private RateStatus status;

  public Rate() {

  }

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
  //TODO: dateTimeRange must be valid--on same day and endTime >= startTime
  public boolean isAvailable(DateTimeRange dateTimeRange) {
    return dateTimeRange.getDayOfWeek()
      .filter(days::contains)
      .filter(dow ->
        dateTimeRange.getStartTime()
          .map(st -> st.compareTo(startTime) >= 0)
          .orElse(false))
      .filter(dow ->
        dateTimeRange.getEndTime()
          .map(end -> end.compareTo(endTime) <= 0)
          .orElse(false))
      .isPresent();
  }

  public RateOuterClass.Rate asAvailablePbRate() {
    return RateOuterClass.Rate.newBuilder()
      .setPrice(getPrice())
      .build();
  }

}
