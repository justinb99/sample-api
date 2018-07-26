package justinb99.sampleapi.service;

import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {

  @Mock
  private Injector injector;
  @Mock
  private JerseyServer expectedServer;

  @Test
  public void startServer() throws Exception {
    when(injector.getInstance(JerseyServer.class)).thenReturn(expectedServer);
    var server = Main.startServer(injector);
    assertSame(expectedServer, server);
    verify(expectedServer).start();
  }

}
