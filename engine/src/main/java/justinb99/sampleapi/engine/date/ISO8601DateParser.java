package justinb99.sampleapi.engine.date;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

public class ISO8601DateParser {

  private StdDateFormat stdDateFormat = new StdDateFormat();

  public Optional<OffsetDateTime> parse(String input) {
    return Optional.ofNullable(input)
      .flatMap(inpt -> {
        try {
          return Optional.of(stdDateFormat.parse(inpt));
        } catch (ParseException pe) {
          return Optional.empty();
        }
      })
      .map(Date::toInstant)
      .map(i -> i.atOffset(ZoneOffset.UTC));
  }

}
