package justinb99.sampleapi.engine.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StreamPropertyProviderTest {

  @Test
  public void getProperty() {
    var target = new SystemPropertyProvider();
    var property = target.getProperty("port", "8080");
    assertEquals("8080", property);

    System.setProperty("port", "9000");
    property = target.getProperty("port", "8080");
    assertEquals("9000", property);
  }

}
