package justinb99.sampleapi.service.servlet;

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
