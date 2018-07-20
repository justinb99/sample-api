package justinb99.sampleapi.engine.service;

import justinb99.sampleapi.engine.model.Rate;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class RateService {

  private final List<Rate> rates;

  @Inject
  public RateService(List<Rate> rates) {
     this.rates = rates;
  }

  //Per spec, A rate must completely encapsulate a datetime range for it to be available.
  //Rates will never overlap.
  //TODO: only handling simple case, both instants are on the same day
  public Optional<Rate> getRate(OffsetDateTime utcStart, OffsetDateTime utcEnd) {
    /*
    return rates.stream()
      .filter(rate -> rate.isAvailable(utcStart, utcEnd))
      .findFirst();
    //for same day, get day of week,
    */
    return Optional.empty();
  }

}
