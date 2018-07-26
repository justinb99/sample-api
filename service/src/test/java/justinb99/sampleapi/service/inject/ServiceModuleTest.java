package justinb99.sampleapi.service.inject;

import com.codahale.metrics.MetricRegistry;
import justinb99.sampleapi.engine.util.SystemPropertyProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceModuleTest {

  @Mock
  private MetricRegistry metricRegistry;
  @Mock
  private SystemPropertyProvider systemPropertyProvider;
  private ServiceModule target;

  @Before
  public void before() {
    target = new ServiceModule();
  }

  //Not much to inspect
  @Test
  public void buildResourceConfig() {
    var resourceConfig = target.buildResourceConfig(metricRegistry);
    assertNotNull(resourceConfig);
  }

  @Test
  public void buildConfiguration() {
    when(systemPropertyProvider.getProperty("port", "8080")).thenReturn("8080");
    var jerseyConfiguration = target.buildConfiguration(systemPropertyProvider, new ResourceConfig());
    assertNotNull(jerseyConfiguration);
    verify(systemPropertyProvider).getProperty("port", "8080");
  }

}
