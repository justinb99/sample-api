package justinb99.sampleapi.engine.util;

public class SystemPropertyProvider {

  public String getProperty(String key, String def) {
    return System.getProperty(key, def);
  }

}
