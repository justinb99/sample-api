package justinb99.sampleapi.service.servlet;

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class serves up static resources that are in the jar's compiled resources.
 * It may not be the most efficient implementation but I spent significantly
 * less time developing this class than I spent trying to use the DefaultServlet
 * or other standard webapp practices for serving static resources
 * in conjunction with a non-webapp, non web.xml based, single executable jar.
 *
 * An instance of a ResourceServlet is bound to a specific "/path" in the
 * resources hierarchy.
 */
public class ResourceServlet extends HttpServlet {

  private final ResourceProvider resourceProvider;
  private final StreamCopier streamCopier;
  private final String resourcePath;

  public ResourceServlet(
    ResourceProvider resourceProvider,
    StreamCopier streamCopier,
    String resourcePath
  ) {
    this.resourceProvider = resourceProvider;
    this.streamCopier = streamCopier;
    this.resourcePath = resourcePath;
  }

  /**
   * Combines the configured "resourcePath" with "getPathInfo" portion of the incoming request
   * to interpret as a resource to serve up.  If the resource exists, it copies it to the
   * response's OutputStream.  If the resource does not exist, it has the response send a
   * 404 not found error.
   *
   * @param request Incoming request
   * @param response Outgoing response
   * @throws ServletException not thrown
   * @throws IOException on error copying the resource InputStream to the response OutputStream
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    var resource = resourcePath + request.getPathInfo();
    var resourceStream = resourceProvider.getResourceAsStream(resource);
    if (resourceStream != null) {
      streamCopier.copy(resourceStream, response.getOutputStream());
    } else {
      response.sendError(HttpStatus.NOT_FOUND_404);
    }
  }

}
