package justinb99.sampleapi.service.servlet;

import justinb99.sampleapi.engine.util.ResourceProvider;
import justinb99.sampleapi.engine.util.StreamCopier;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServletTest {

  private static final String RESOURCE_PATH = "/test";

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private ServletOutputStream responseStream;
  @Mock
  private ResourceProvider resourceProvider;
  @Mock
  private StreamCopier streamCopier;
  @Mock
  private InputStream resourceStream;
  private ResourceServlet target;

  @Before
  public void before() {
    target = new ResourceServlet(resourceProvider, streamCopier, RESOURCE_PATH);
  }

  @Test
  public void doGet_valid_resource() throws Exception {
    var resource = "/index.html";
    when(request.getPathInfo()).thenReturn(resource);
    when(resourceProvider.getResourceAsStream(RESOURCE_PATH + resource))
      .thenReturn(resourceStream);
    when(response.getOutputStream()).thenReturn(responseStream);

    target.doGet(request, response);

    verify(resourceProvider).getResourceAsStream(RESOURCE_PATH + resource);
    verify(response).getOutputStream();
    verify(streamCopier).copy(same(resourceStream), same(responseStream));
  }

  @Test
  public void doGet_resource_does_not_exist() throws Exception {
    var resource = "/index.html";
    when(request.getPathInfo()).thenReturn(resource);
    when(resourceProvider.getResourceAsStream(RESOURCE_PATH + resource))
      .thenReturn(null);

    target.doGet(request, response);

    verify(resourceProvider).getResourceAsStream(RESOURCE_PATH + resource);
    verify(response).sendError(HttpStatus.NOT_FOUND_404);
    verify(streamCopier, never()).copy(any(), any());
  }
}
