package justinb99.sampleapi.engine.inject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.engine.model.RateConfig;
import justinb99.sampleapi.engine.model.RatesConfig;
import justinb99.sampleapi.engine.util.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EngineModule extends AbstractModule {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void configure() {
    var objectMapper = createObjectMapper();

    bind(ObjectMapper.class)
      .toInstance(objectMapper);

    bind(XmlMapper.class)
      .toInstance(createXmlMapper());

    bind(new TypeLiteral<List<Rate>>() {})
      .toInstance(loadConfiguredRates(new ResourceProvider(), objectMapper));
  }

  ObjectMapper createObjectMapper() {
    return configureObjectMapper(new ObjectMapper());
  }

  XmlMapper createXmlMapper() {
    var xmlMapper = new XmlMapper();
    xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    return configureObjectMapper(xmlMapper);
  }

  private <T extends ObjectMapper> T configureObjectMapper(T objectMapper) {
    objectMapper
      .setSerializationInclusion(Include.NON_NULL)
      .registerModule(new ProtobufModule());
    return objectMapper;
  }

  List<Rate> loadConfiguredRates(
    ResourceProvider resourceProvider,
    ObjectMapper objectMapper
  ) {
    var ratesStream = resourceProvider.getResourceAsStream("/rates.json");
    var jsonReader = objectMapper.readerFor(RatesConfig.class);

    RatesConfig config;
    try {
      config = jsonReader.readValue(ratesStream);
    } catch (IOException e) {
      logger.error("Failed to deserialze configured Rates. Cannot start.", e);
      throw new RuntimeException(e);
    }

    return config.getRates().stream()
      .map(RateConfig::asRate)
      .collect(toList());
  }

}
