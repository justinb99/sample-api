package justinb99.sampleapi.engine.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RateConfig {

  private String days;
  private String times;
  private Integer price;

  public String getDays() {
    return days;
  }

  public void setDays(String days) {
    this.days = days;
  }

  public String getTimes() {
    return times;
  }

  public void setTimes(String times) {
    this.times = times;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  //TODO test remainder
  public Rate asRate() {
    var rate = new Rate();

    Set<DayOfWeek> days = Arrays.stream(getDays().split(","))
      .map(Day::valueOf)
      .map(Day::getDayOfWeek)
      .collect(Collectors.toSet());
    rate.setDays(days);

    var splitTimes = getTimes().split("-");
    rate.setStartTime(parse4DigitTime(splitTimes[0]));
    rate.setEndTime(parse4DigitTime(splitTimes[1]));

    rate.setPrice(getPrice());

    return rate;
  }

  private LocalTime parse4DigitTime(String time) {
    int hour = Integer.valueOf(time.substring(0, 2));
    int minute = Integer.valueOf(time.substring(2, 4));
    return LocalTime.of(hour, minute);
  }
}
