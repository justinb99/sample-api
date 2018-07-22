package justinb99.sampleapi.engine.module;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.engine.model.RateConfig;
import justinb99.sampleapi.engine.model.RatesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EngineModule extends AbstractModule {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Provides
  @Singleton
  public ObjectMapper provideObjectMapper() {
    return new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL);
  }

//  @Provides
//  public List<Rate> provideConfiguredRates(ObjectMapper objectMapper) {
//    var ratesStream = getClass().getResourceAsStream("/rates.json");
//    var jsonReader = objectMapper.readerFor(RatesConfig.class);
//
//    RatesConfig config;
//    try {
//      config = jsonReader.readValue(ratesStream);
//    } catch (IOException e) {
//      logger.error("Failed to deserialze configured Rates", e);
//      return Collections.emptyList();
//    }
//
//    return config.getRates().stream()
//      .map(RateConfig::asRate)
//      .collect(toList());
//  }

}
