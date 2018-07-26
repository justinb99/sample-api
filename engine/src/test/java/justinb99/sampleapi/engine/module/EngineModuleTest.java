package justinb99.sampleapi.engine.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.engine.model.RateConfig;
import justinb99.sampleapi.engine.model.RatesConfig;
import justinb99.sampleapi.engine.util.ResourceProvider;
import justinb99.sampleapi.schema.RateOuterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EngineModuleTest {

  private static final Integer PRICE = 1234;

  @Mock
  private ResourceProvider resourceProvider;

  private RateOuterClass.Rate rate, unavailableRate;
  private EngineModule target;

  @Before
  public void before() {
    target = new EngineModule();

    rate = RateOuterClass.Rate.newBuilder()
      .setPrice(PRICE)
      .build();

    unavailableRate = RateOuterClass.Rate.newBuilder()
      .setStatus(RateOuterClass.Rate.Status.unavailable)
      .build();


  }

  @Test
  public void object_mapper_configuration() {
    var objectMapper = target.createObjectMapper();

    var expectedJson = objectMapper.createObjectNode()
      .put("price", PRICE);

    ObjectNode actualJson = objectMapper.valueToTree(rate);

    assertEquals(expectedJson, actualJson);
  }

  @Test
  public void object_mapper_configuration_unavailable() {
    var objectMapper = target.createObjectMapper();

    var expectedJson = objectMapper.createObjectNode()
      .put("status", "unavailable");

    ObjectNode actualJson = objectMapper.valueToTree(unavailableRate);

    assertEquals(expectedJson, actualJson);
  }

  @Test
  public void xml_mapper_configuration() throws Exception {
    var expectedXml = "<?xml version='1.0' encoding='UTF-8'?><Rate><price>" +
      PRICE +
      "</price></Rate>";

    var xmlMapper = target.createXmlMapper();
    var xml = xmlMapper.writeValueAsString(rate);

    assertEquals(expectedXml, xml);
  }

  @Test
  public void xml_mapper_configuration_unavailable() throws Exception {
    var expectedXml =
      "<?xml version='1.0' encoding='UTF-8'?><Rate><status>unavailable</status></Rate>";

    var xmlMapper = target.createXmlMapper();
    var xml = xmlMapper.writeValueAsString(unavailableRate);

    assertEquals(expectedXml, xml);
  }

  @Test
  public void loadConfiguredRates_no_rates() {
    var objectMapper = new ObjectMapper();
    RuntimeException caught = null;

    when(resourceProvider.getResourceAsStream(any())).thenReturn(null);

    try {
      target.loadConfiguredRates(resourceProvider, objectMapper);
    } catch (RuntimeException e) {
      caught = e;
    }

    assertThat(caught.getCause(), instanceOf(IOException.class));
  }

  @Test
  public void loadConfiguredRates_successful() throws Exception {
    var objectMapper = mock(ObjectMapper.class);
    var ratesReader = mock(ObjectReader.class);
    var resourceStream = mock(InputStream.class);

    var ratesConfig = new RatesConfig();
    var rateConfig1 = mock(RateConfig.class);
    var rateConfig2 = mock(RateConfig.class);
    ratesConfig.setRates(List.of(rateConfig1, rateConfig2));

    var rate1 = mock(Rate.class);
    var rate2 = mock(Rate.class);
    when(rateConfig1.asRate()).thenReturn(rate1);
    when(rateConfig2.asRate()).thenReturn(rate2);

    when(resourceProvider.getResourceAsStream("/rates.json")).thenReturn(resourceStream);
    when(objectMapper.readerFor(RatesConfig.class)).thenReturn(ratesReader);
    when(ratesReader.readValue(same(resourceStream))).thenReturn(ratesConfig);

    List<Rate> rates = target.loadConfiguredRates(resourceProvider, objectMapper);
    assertEquals(List.of(rate1, rate2), rates);
  }


}
