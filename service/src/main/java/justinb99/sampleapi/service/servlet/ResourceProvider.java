package justinb99.sampleapi.service.servlet;

import java.io.InputStream;

public class ResourceProvider {

  public InputStream getResourceAsStream(String resource) {
    return getClass().getResourceAsStream(resource);
  }

}
