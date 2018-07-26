package justinb99.sampleapi.engine.date;

import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
      .of(2018, 7, 19, 23, 49, 8, 0, ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  @Test
  public void parse_with_3_millis_Z() {
    var parsed = target.parse("2018-07-19T23:49:08.123Z");

    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 123000000, ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  @Test
  public void parse_with_1_millis_Z() {
    var parsed = target.parse("2018-07-19T23:49:08.1Z");

    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  @Test
  public void parse_with_offset() {
    var parsed = target.parse("2018-07-19T23:49:08.1+1234");

    var offset = ZoneOffset.ofHoursMinutes(12, 34);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant().atOffset(ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  @Test
  public void parse_with_offset_colon() {
    var parsed = target.parse("2018-07-19T23:49:08.1+12:34");

    var offset = ZoneOffset.ofHoursMinutes(12, 34);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant().atOffset(ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  //Parsing a date with just offset hours succeeds with StdDateFormat but fails with deprecated ISO8601DateFormat
  @Test
  public void parse_with_offset_hours() {
    var parsed = target.parse("2018-07-19T23:49:08.1-12");

    var offset = ZoneOffset.ofHoursMinutes(-12, 0);
    var expected = OffsetDateTime
      .of(2018, 7, 19, 23, 49, 8, 100000000, offset)
      .toInstant().atOffset(ZoneOffset.UTC);

    assertEquals(expected, parsed.get());
  }

  //Parsing a compact format fails with StdDateFormat but succeeds with deprecated ISO8601DateFormat
  @Test
  public void parse_compact() {
    var parsed = target.parse("20180719T234908Z");
    assertFalse(parsed.isPresent());
  }

  @Test
  public void parse_null() {
    assertFalse(target.parse(null).isPresent());
  }

  @Test
  public void parse_invalid() {
    var invalid = "2018-7-1T1000";
    assertFalse(target.parse(invalid).isPresent());
  }

  @Test
  public void parseRange_valid() {
    var start = "2018-07-19T10:00:00Z";
    var end = "2018-07-19T11:00:00Z";

    var expectedStart = OffsetDateTime
      .of(2018, 7, 19, 10, 0, 0, 0, ZoneOffset.UTC);
    var expectedEnd = expectedStart.plusHours(1L);

    var parsed = target.parseRange(start, end);
    assertEquals(expectedStart, parsed.get().getStart());
    assertEquals(expectedEnd, parsed.get().getEnd());
  }

  @Test
  public void parseRange_invalid_start() {
    var start = "2018-7-1T1000";
    var end = "2018-07-19T11:00:00Z";

    var parsed = target.parseRange(start, end);
    assertFalse(parsed.isPresent());
  }

  @Test
  public void parseRange_invalid_end() {
    var start = "2018-07-19T10:00:00Z";
    var end = "2018-7-1T1100";

    var parsed = target.parseRange(start, end);
    assertFalse(parsed.isPresent());
  }

}
