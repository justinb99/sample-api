package justinb99.sampleapi.engine.module;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
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

  @Override
  protected void configure() {
    var objectMapper = createObjectMapper();

    bind(ObjectMapper.class)
      .toInstance(objectMapper);

    bind(new TypeLiteral<List<Rate>>() {})
      .toInstance(loadConfiguredRates(objectMapper));
  }

  ObjectMapper createObjectMapper() {
    return new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .registerModule(new ProtobufModule());
  }

  List<Rate> loadConfiguredRates(ObjectMapper objectMapper) {
    var ratesStream = getClass().getResourceAsStream("/rates.json");
    var jsonReader = objectMapper.readerFor(RatesConfig.class);

    RatesConfig config;
    try {
      config = jsonReader.readValue(ratesStream);
    } catch (IOException e) {
      logger.error("Failed to deserialze configured Rates", e);
      return Collections.emptyList();
    }

    return config.getRates().stream()
      .map(RateConfig::asRate)
      .collect(toList());
  }

}
