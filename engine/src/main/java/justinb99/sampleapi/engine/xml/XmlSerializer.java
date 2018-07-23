package justinb99.sampleapi.engine.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.inject.Inject;

public class XmlSerializer {

  private final XmlMapper xmlMapper;

  @Inject
  public XmlSerializer(XmlMapper xmlMapper) {
    this.xmlMapper = xmlMapper;
  }

  public String toXmlString(Object object) {
    try {
      return xmlMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }
}
