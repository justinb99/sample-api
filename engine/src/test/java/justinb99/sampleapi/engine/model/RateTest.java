package justinb99.sampleapi.engine.model;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateTest {

  private static final LocalTime START_TIME = LocalTime.of(10, 0);
  private static final LocalTime END_TIME = LocalTime.of(14, 0);

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
  public void isAvailable_no_day_of_week() {
    target.setDays(Set.of(MONDAY));
    when(dateTimeRange.getDayOfWeek()).thenReturn(Optional.empty());

    assertFalse(target.isAvailable(dateTimeRange));
  }

  @Test
  public void isAvailable_for_days() {
    when(dateTimeRange.getStartTime()).thenReturn(START_TIME.plusHours(1));
    when(dateTimeRange.getEndTime()).thenReturn(END_TIME.minusHours(1));

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
    when(dateTimeRange.getStartTime()).thenReturn(START_TIME);
    when(dateTimeRange.getEndTime()).thenReturn(END_TIME);

    assertTrue(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_just_before_or_just_after() {
    when(dateTimeRange.getStartTime()).thenReturn(START_TIME.minusNanos(1L));
    when(dateTimeRange.getEndTime()).thenReturn(END_TIME);
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));

    when(dateTimeRange.getStartTime()).thenReturn(START_TIME);
    when(dateTimeRange.getEndTime()).thenReturn(END_TIME.plusNanos(1L));
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

  @Test
  public void isAvailable_inverted_time_range() {
    when(dateTimeRange.getStartTime()).thenReturn(END_TIME);
    when(dateTimeRange.getEndTime()).thenReturn(START_TIME);
    assertFalse(rangeIsAvailableForDays(Set.of(MONDAY)));
  }

}
