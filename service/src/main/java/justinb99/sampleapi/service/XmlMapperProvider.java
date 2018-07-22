package justinb99.sampleapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class XmlMapperProvider implements ContextResolver<XmlMapper> {

  private final XmlMapper xmlMapper;

  @Inject
  public XmlMapperProvider(XmlMapper xmlMapper) {
    this.xmlMapper = xmlMapper;
  }

  @Override
  public XmlMapper getContext(Class<?> aClass) {
    return xmlMapper;
  }

}
