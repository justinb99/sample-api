package justinb99.sampleapi.engine.xml;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import justinb99.sampleapi.schema.RateOuterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerTest {

  @Mock
  private XmlMapper xmlMapper;
  @Mock
  private JsonMappingException jsonException;
  private RateOuterClass.Rate rate;
  private XmlSerializer target;

  @Before
  public void before() {
    target = new XmlSerializer(xmlMapper);
    rate = RateOuterClass.Rate.newBuilder().build();
  }

  @Test
  public void toXmlString() throws Exception {
    var expectedXml = "<rate></rate>";
    when(xmlMapper.writeValueAsString(same(rate))).thenReturn(expectedXml);
    var actual = target.toXmlString(rate);
    assertEquals(expectedXml, actual);
    verify(xmlMapper).writeValueAsString(same(rate));
  }

  @Test
  public void toXmlString_Exception() throws Exception {
    when(xmlMapper.writeValueAsString(same(rate))).thenThrow(jsonException);
    JsonMappingException caught = null;

    try {
      target.toXmlString(rate);
    } catch (RuntimeException e) {
      caught = (JsonMappingException)e.getCause();
    }

    assertSame(caught, jsonException);
    verify(xmlMapper).writeValueAsString(same(rate));
  }
}
