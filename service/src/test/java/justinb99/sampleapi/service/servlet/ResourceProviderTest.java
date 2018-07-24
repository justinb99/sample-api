package justinb99.sampleapi.service.servlet;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ResourceProviderTest {

  private ResourceProvider target;

  @Before
  public void before() {
    target = new ResourceProvider();
  }

  @Test
  public void getResourceAsStream() throws Exception {
    var stream = target.getResourceAsStream("/TestResource.txt");
    var resourceString = IOUtils.toString(stream, StandardCharsets.UTF_8);
    assertEquals("Hello World", resourceString);
  }

}
