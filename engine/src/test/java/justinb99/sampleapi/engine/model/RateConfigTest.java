package justinb99.sampleapi.engine.model;

import org.junit.Test;

import java.time.LocalTime;

import static java.time.DayOfWeek.*;
import static justinb99.sampleapi.engine.util.CollectionUtil.toSet;
import static org.junit.Assert.assertEquals;

public class RateConfigTest {

  @Test
  public void asRate() {
    var target = new RateConfig();
    target.setDays("mon,tues,thurs");
    target.setTimes("0900-2100");
    target.setPrice(1500);

    var rate = target.asRate();
    var expectedDays = toSet(MONDAY, TUESDAY, THURSDAY);
    assertEquals(expectedDays, rate.getDays());

//    var startTime = LocalTime.of(9, 0);
//    assertEquals(startTime, rate.getStartTime());

    assertEquals(1500, rate.getPrice().intValue());
  }

}
