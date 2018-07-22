package justinb99.sampleapi.engine.service;

import justinb99.sampleapi.engine.date.ISO8601DateParser;
import justinb99.sampleapi.engine.model.DateTimeRange;
import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.engine.model.RateStatus;
import justinb99.sampleapi.schema.RateOuterClass;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RateEngine {

  private final List<Rate> rates;
  private final ISO8601DateParser dateParser;

  @Inject
  public RateEngine(
    List<Rate> rates,
    ISO8601DateParser dateParser
  ) {
     this.rates = rates;
     this.dateParser = dateParser;
  }

  public RateOuterClass.Rate getRate(String start, String end) {
    return dateParser.parse(start)
      .flatMap(s -> dateParser.parse(end).map(e -> Pair.of(s, e)))
      .map(p -> new DateTimeRange(p.getLeft(), p.getRight()))
      .flatMap(this::getRate)
      .map(Rate::asAvailablePbRate)
      .orElse(getUnavailableRate());
  }

  Optional<Rate> getRate(DateTimeRange dateTimeRange) {
    return rates.stream()
      .filter(rate -> rate.isAvailable(dateTimeRange))
      .findFirst();
  }

  RateOuterClass.Rate getUnavailableRate() {
    return RateOuterClass.Rate.newBuilder()
      .setStatus(RateOuterClass.Rate.Status.unavailable)
      .build();
  }

}