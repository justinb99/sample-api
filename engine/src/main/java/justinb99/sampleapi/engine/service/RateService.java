package justinb99.sampleapi.engine.service;

import justinb99.sampleapi.engine.model.DateTimeRange;
import justinb99.sampleapi.engine.model.Rate;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class RateService {

  private final List<Rate> rates;

  //TODO: will need to provide List<Rate>
  @Inject
  public RateService(List<Rate> rates) {
     this.rates = rates;
  }

  public Optional<Rate> getRate(DateTimeRange dateTimeRange) {
    return rates.stream()
      .filter(rate -> rate.isAvailable(dateTimeRange))
      .findFirst();
  }

}
