package justinb99.sampleapi.engine.date;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import justinb99.sampleapi.engine.model.DateTimeRange;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

/**
 * Uses Jackson's StdDateFormat to parse date strings.
 * Jackson's date parsing is the best ISO8601 date
 * parser I've encountered and seems to handle more formats than
 * OffsetDateTime.parse().  By using it, it should maintain consistency
 * when parsing dates in JSON messages since I assume Jackson's JSON parser
 * will use similar date parsing internally.  Unfortunately the parsing is not perfect
 * and can't handle a valid ISO8601 compact date like "20180719T234908Z".  Jackson's
 * previous ISO8601DateFormat could handle a compact date, but could not
 * handle when just offset hours are present, like "2018-07-19T23:49:08.1-12".
 * Additionally, ISO8601DateFormat was deprecated in Jackson 2.9 in favor of
 * StdDateFormat.  Thus I chose to use StdDateFormat here.
 */
public class ISO8601DateParser {

  private DateFormat dateFormat = new StdDateFormat();

  Optional<OffsetDateTime> parse(String input) {
    return Optional.ofNullable(input)
      .flatMap(inpt -> {
        try {
          return Optional.of(dateFormat.parse(inpt));
        } catch (ParseException pe) {
          return Optional.empty();
        }
      })
      .map(Date::toInstant)
      .map(i -> i.atOffset(ZoneOffset.UTC));
  }

  public Optional<DateTimeRange> parseRange(String start, String end) {
    return parse(start)
      .flatMap(s -> parse(end).map(e -> Pair.of(s, e)))
      .map(p -> new DateTimeRange(p.getLeft(), p.getRight()));
  }

}
