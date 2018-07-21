package justinb99.sampleapi.engine.model;

import java.time.OffsetDateTime;

public class DateTimeRange {

  private OffsetDateTime start;
  private OffsetDateTime end;

  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public OffsetDateTime getEnd() {
    return end;
  }

  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }

}
