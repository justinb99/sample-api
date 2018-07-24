package justinb99.sampleapi.service;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyMetricServletContextListener extends MetricsServlet.ContextListener {

  private final MetricRegistry metricRegistry;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  public MyMetricServletContextListener(MetricRegistry metricRegistry) {
    logger.info("\n\n\nMyMetricServletContextListener metricRegistry={}", System.identityHashCode(metricRegistry));
    this.metricRegistry = metricRegistry;
  }

  @Override
  protected MetricRegistry getMetricRegistry() {
    return metricRegistry;
  }
}
