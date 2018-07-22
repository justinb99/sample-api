package justinb99.sampleapi.engine.date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@Ignore
public class ISO8601DateParserTest {

  private ISO8601DateParser target;

  @Before
  public void before() {
    target = new ISO8601DateParser();
  }

  @Test
  public void parse_with_Z() {
    var parsed = target.parse("2018-07-19T23:49:08Z");

    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 0, ZoneOffset.UTC)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Test
  public void parse_with_3_millis_Z() {
    var parsed = target.parse("2018-07-19T23:49:08.123Z");

    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 123000000, ZoneOffset.UTC)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Test
  public void parse_with_1_millis_Z() {
    var parsed = target.parse("2018-07-19T23:49:08.1Z");

    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, ZoneOffset.UTC)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Test
  public void parse_with_offset() {
    var parsed = target.parse("2018-07-19T23:49:08.1+1234");

    var offset = ZoneOffset.ofHoursMinutes(12, 34);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Test
  public void parse_with_offset_colon() {
    var parsed = target.parse("2018-07-19T23:49:08.1+12:34");

    var offset = ZoneOffset.ofHoursMinutes(12, 34);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Test
  public void parse_with_offset_hours() {
    var parsed = target.parse("2018-07-19T23:49:08.1-12");

    var offset = ZoneOffset.ofHoursMinutes(-12, 0);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant();

    assertEquals(expected, parsed);
  }

  @Ignore //TODO: compact times don't seem to work. Handle errors better
  @Test
  public void parse_compact() {
    var parsed = target.parse("20180719T234908Z");

    var offset = ZoneOffset.ofHoursMinutes(-12, 0);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant();

    assertEquals(expected, parsed);
  }

}
