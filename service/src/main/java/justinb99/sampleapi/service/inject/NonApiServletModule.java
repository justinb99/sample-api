package justinb99.sampleapi.service.inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.google.inject.servlet.ServletModule;
import justinb99.sampleapi.engine.util.ResourceProvider;
import justinb99.sampleapi.engine.util.StreamCopier;
import justinb99.sampleapi.service.servlet.ResourceServlet;

public class NonApiServletModule extends ServletModule {

  private final MetricRegistry metricRegistry;

  NonApiServletModule(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  @Override
  protected void configureServlets() {
    serve("/ping").with(new PingServlet());
    serve("/metrics").with(new MetricsServlet(metricRegistry));

    var docsResourceServlet
      = new ResourceServlet(new ResourceProvider(), new StreamCopier(), "/docs");
    serve("/docs/*").with(docsResourceServlet);
  }

}
