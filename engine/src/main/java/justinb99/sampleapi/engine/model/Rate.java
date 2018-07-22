package justinb99.sampleapi.engine.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.time.*;
import java.util.Optional;
import java.util.Set;

@XmlRootElement(name = "rate")
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

  @XmlElement
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Rate withPrice(Integer price) {
    setPrice(price);
    return this;
  }

  @XmlElement
  public RateStatus getStatus() {
    return status;
  }

  public void setStatus(RateStatus status) {
    this.status = status;
  }

  public Rate withStatus(RateStatus rateStatus) {
    setStatus(rateStatus);
    return this;
  }

  //Per spec, A rate must completely encapsulate a datetime range for it to be available.
  //Rates will never overlap.
  //assumes start and end are both UTC
  //TODO: dateTimeRange must be valid--on same day and endTime >= startTime
  public boolean isAvailable(DateTimeRange dateTimeRange) {
    return Optional.of(dateTimeRange.getDayOfWeek())
      .filter(days::contains)
      .filter(dow -> dateTimeRange.getStartTime().compareTo(startTime) >= 0)
      .filter(dow -> dateTimeRange.getEndTime().compareTo(endTime) <= 0)
      .isPresent();
  }

  public Rate justPrice() {
    return new Rate().withPrice(getPrice());
  }

}
