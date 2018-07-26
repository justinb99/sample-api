package justinb99.sampleapi.service.resource.v1;

import justinb99.sampleapi.engine.RateEngine;
import justinb99.sampleapi.engine.xml.XmlSerializer;
import justinb99.sampleapi.schema.RateOuterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateResourceTest {

  private static final String START = "start";
  private static final String END = "end";

  @Mock
  private RateEngine rateEngine;
  @Mock
  private XmlSerializer xmlSerializer;
  private RateOuterClass.Rate expectedRate;
  private RateResource target;

  @Before
  public void before() {
    target = new RateResource(rateEngine, xmlSerializer);

    expectedRate = RateOuterClass.Rate.newBuilder()
      .setPrice(1234)
      .build();
    when(rateEngine.getRate(START, END)).thenReturn(expectedRate);
  }

  @Test
  public void getRate() {
    var rate = target.getRate(START, END);
    assertSame(expectedRate, rate);
  }

  @Test
  public void getRateJson() {
    var rate = target.getRateJson(START, END);
    assertSame(expectedRate, rate);
  }

  @Test
  public void getRateXml() {
    var expectedXml = "<Rate></Rate>";
    when(xmlSerializer.toXmlString(same(expectedRate))).thenReturn(expectedXml);
    var response = target.getRateXml(START, END);
    assertEquals(200, response.getStatus());
    assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
    assertEquals(expectedXml, response.getEntity());
    verify(xmlSerializer).toXmlString(same(expectedRate));
  }

  @Test
  public void getRateProtoBinary() throws Exception {
    var pbBytes = target.getRateProtoBinary(START, END);
    var actualRate = RateOuterClass.Rate.parseFrom(pbBytes);
    assertEquals(expectedRate, actualRate);
  }

}
