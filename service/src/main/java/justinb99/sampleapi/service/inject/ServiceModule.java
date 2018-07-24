package justinb99.sampleapi.service.inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.ServletModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    var theMetricRegistry = new MetricRegistry();
    bind(MetricRegistry.class).toInstance(theMetricRegistry);

    var resourceConfig = buildResourceConfig(theMetricRegistry);
    JerseyConfiguration configuration = buildConfiguration(resourceConfig);

//    install(new JerseyModule(configuration));
    Provider<Injector> injectorProvider = getProvider(Injector.class);

    install(new ServletModule() {
      @Override
      protected void configureServlets() {
        serve("/ping").with(new PingServlet());
        serve("/metrics").with(new MetricsServlet(theMetricRegistry));
      }
    });

    Constructor<JerseyServer> constructor = null;
    try {
      constructor = JerseyServer.class.getDeclaredConstructor(JerseyConfiguration.class, Supplier.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    constructor.setAccessible(true);
    Supplier<Injector> injectorSupplier = injectorProvider::get;
    JerseyServer jerseyServer = null;
    try {
      jerseyServer = constructor.newInstance(configuration, injectorSupplier);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    bind(JerseyServer.class).toInstance(jerseyServer);
    bind(JerseyConfiguration.class).toInstance(configuration);
  }

  ResourceConfig buildResourceConfig(MetricRegistry metricRegistry) {
    return new ResourceConfig()
      .register(new InstrumentedResourceMethodApplicationListener(metricRegistry));
  }

  JerseyConfiguration buildConfiguration(ResourceConfig resourceConfig) {
    int port = Integer.valueOf(System.getProperty("port", "8080"));
    return JerseyConfiguration.builder()
      .addPackage("justinb99.sampleapi.service.resource")
      .addPort(port)
      .registerClasses(ObjectMapperProvider.class)
      .registerClasses(JacksonFeature.class)
      .withResourceConfig(resourceConfig)
      .build();
  }

}
