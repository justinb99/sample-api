package justinb99.sampleapi.engine.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

public class DateTimeRange {

  private OffsetDateTime start;
  private OffsetDateTime end;

  public DateTimeRange() {
  }

  public DateTimeRange(OffsetDateTime start, OffsetDateTime end) {
    this.start = start;
    this.end = end;
  }

  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public Optional<LocalTime> getStartTime() {
    if (endBeforeStart())
      return Optional.empty();
    return Optional.of(start.toLocalTime());
  }

  public OffsetDateTime getEnd() {
    return end;
  }

  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }

  public Optional<LocalTime> getEndTime() {
    if (endBeforeStart())
      return Optional.empty();
    return Optional.of(end.toLocalTime());
  }

  private boolean endBeforeStart() {
    return end.compareTo(start) < 0;
  }

  /**
   * Get the DayOfWeek if the DateTimeRange is valid:
   * - start and end are on the same date
   * - end is >= start
   *
   * Assumes start and end are in the same time zone because the instance
   * was constructed from valid user input
   *
   * @return DayOfWeek if the DateTimeRange is valid, else empty
   */
  public Optional<DayOfWeek> getDayOfWeek() {
    if (start.toLocalDate().equals(end.toLocalDate()))
      return Optional.of(start.getDayOfWeek());
    else
      return Optional.empty();
  }

}
