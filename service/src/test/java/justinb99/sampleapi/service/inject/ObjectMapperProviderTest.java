package justinb99.sampleapi.service.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ObjectMapperProviderTest {

  @Mock
  private ObjectMapper objectMapper;
  private ObjectMapperProvider target;

  @Before
  public void before() {
    target = new ObjectMapperProvider(objectMapper);
  }

  @Test
  public void getContext() {
    assertSame(objectMapper, target.getContext(null));
  }

}
