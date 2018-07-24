package justinb99.sampleapi.service.inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NonApiServletModule extends ServletModule {

  private final MetricRegistry metricRegistry;

  public NonApiServletModule(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  @Override
  protected void configureServlets() {
    serve("/ping").with(new PingServlet());
    serve("/metrics").with(new MetricsServlet(metricRegistry));
    serve("/docs/*").with(StaticServlet.class);
  }

  @Singleton
  public static class StaticServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String resource = "/docs" + request.getPathInfo();
      logger.info("Getting resource {}", resource);
      var resourceStream = getClass().getResourceAsStream(resource);
      if (resourceStream != null) {
        IOUtils.copy(resourceStream, response.getOutputStream());
//      response.getWriter().print("Hello!\n" +
//        "serveletPath=" + request.getServletPath() + "\n" +
//        "contextPath=" + request.getContextPath() + "\n" +
//        "pathInfo=" + request.getPathInfo() + "\n" +
//        "queryString=" + request.getQueryString());
      } else {
        response.sendError(HttpStatus.NOT_FOUND_404);
      }
    }
  }

}
