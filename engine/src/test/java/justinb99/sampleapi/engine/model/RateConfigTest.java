package justinb99.sampleapi.engine.model;

import org.junit.Test;

import java.time.LocalTime;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.junit.Assert.assertEquals;

public class RateConfigTest {

  @Test
  public void asRate() {
    var target = new RateConfig();
    target.setDays("mon,tues,thurs");
    target.setTimes("1234-2359");
    target.setPrice(1500);

    var rate = target.asRate();
    var expectedDays = Set.of(MONDAY, TUESDAY, THURSDAY);
    assertEquals(expectedDays, rate.getDays());

    var startTime = LocalTime.of(12, 34);
    assertEquals(startTime, rate.getStartTime());
    var endTime = LocalTime.of(23, 59);
    assertEquals(endTime, rate.getEndTime());

    assertEquals(1500, rate.getPrice().intValue());
  }

}
