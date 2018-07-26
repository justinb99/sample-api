package justinb99.sampleapi.engine.model;

import justinb99.sampleapi.schema.RateOuterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateTest {

  private static final LocalTime START_TIME = LocalTime.of(10, 0);
  private static final LocalTime END_TIME = LocalTime.of(14, 0);
  private static final LocalTime NOON = LocalTime.of(12, 0);

  @Mock
  private DateTimeRange dateTimeRange;
  private Rate target;

  @Before
  public void before() {
    target = new Rate();
    target.setStartTime(START_TIME);
    target.setEndTime(END_TIME);

    when(dateTimeRange.getDayOfWeek()).thenReturn(Optional.of(MONDAY));
  }

  @Test
  public void getters() {
    assertEquals(START_TIME, target.getStartTime());
    assertEquals(END_TIME, target.getEndTime());
  }

  @Test
  public void isAvailable_for_days() {
    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME.plusHours(1)));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(END_TIME.minusHours(1)));

    assertFalse(rangeIsAvailableForDays(Collections.emptySet()));
    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY)));
    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY, WEDNESDAY, FRIDAY)));
    assertFalse(rangeIsAvailableForDays(Set.of(SUNDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY)));
  }

  private boolean rangeIsAvailableForDays(Set<DayOfWeek> targetDays) {
    target.setDays(targetDays);
    return target.isAvailable(dateTimeRange);
  }

  @Test
  public void isAvailable_at_time_bounds() {
    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(END_TIME));

    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_just_before_or_just_after() {
    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME.minusNanos(1L)));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(END_TIME));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));

    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(END_TIME.plusNanos(1L)));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_rate_inverted_times() {
    target.setStartTime(END_TIME);
    target.setEndTime(START_TIME);

    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(NOON));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));

    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(END_TIME));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(END_TIME));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));

    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_range_at_same_time() {
    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(NOON));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(NOON));

    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_start_and_end_at_same_time() {
    target.setStartTime(START_TIME);
    target.setEndTime(START_TIME);

    when(dateTimeRange.getStartTime()).thenReturn(Optional.of(START_TIME));
    when(dateTimeRange.getEndTime()).thenReturn(Optional.of(START_TIME));

    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void asAvailablePbRate() {
    var price = 1234;
    target.setPrice(price);
    var pbRate = target.asAvailablePbRate();
    var expectedPbRate = RateOuterClass.Rate.newBuilder()
      .setPrice(1234)
      .build();
    assertEquals(expectedPbRate, pbRate);
  }

}
