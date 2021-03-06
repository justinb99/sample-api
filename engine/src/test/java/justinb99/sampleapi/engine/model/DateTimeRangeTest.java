package justinb99.sampleapi.engine.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.time.DayOfWeek.SATURDAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DateTimeRangeTest {

  private static final OffsetDateTime START =
    OffsetDateTime.of(2018, 7, 21, 10, 11, 12, 0, ZoneOffset.UTC);
  private static final OffsetDateTime END =
    OffsetDateTime.of(2018, 7, 21, 14, 15, 16, 0, ZoneOffset.UTC);

  private DateTimeRange target;

  @Before
  public void before() {
    target = new DateTimeRange();
    target.setStart(START);
    target.setEnd(END);
  }

  @Test
  public void getStartTime() {
    assertEquals(LocalTime.of(10, 11, 12), target.getStartTime().get());
  }

  @Test
  public void getEndTime() {
    assertEquals(LocalTime.of(14, 15, 16), target.getEndTime().get());
  }

  @Test
  public void getDayOfWeek() {
    assertEquals(SATURDAY, target.getDayOfWeek().get());
  }

  @Test
  public void get_times_inverted() {
    target = new DateTimeRange(END, START);
    assertFalse(target.getStartTime().isPresent());
    assertFalse(target.getEndTime().isPresent());
    assertEquals(SATURDAY, target.getDayOfWeek().get());
  }

  @Test
  public void getDayOfWeek_not_same_day() {
    target = new DateTimeRange(START, END.plusHours(24L));
    assertEquals(LocalTime.of(10, 11, 12), target.getStartTime().get());
    assertEquals(LocalTime.of(14, 15, 16), target.getEndTime().get());
    assertFalse(target.getDayOfWeek().isPresent());
  }

}
