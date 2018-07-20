package justinb99.sampleapi.engine.date;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.ParseException;
import java.time.Instant;

public class ISO8601DateParser {

  private StdDateFormat stdDateFormat = new StdDateFormat();

  public Instant parse(String input) {
    try {
      return stdDateFormat.parse(input).toInstant();
    } catch (ParseException pe) {
      //Todo throw a runtime exception on failure
      return null;
    }
  }

}
