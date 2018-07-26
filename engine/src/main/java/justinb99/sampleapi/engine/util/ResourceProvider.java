package justinb99.sampleapi.engine.util;

import java.io.InputStream;

public class ResourceProvider {

  public InputStream getResourceAsStream(String resource) {
    return getClass().getResourceAsStream(resource);
  }

}
