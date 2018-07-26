package justinb99.sampleapi.service.inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import justinb99.sampleapi.engine.util.SystemPropertyProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    var theMetricRegistry = new MetricRegistry();
    bind(MetricRegistry.class).toInstance(theMetricRegistry);

    install(new NonApiServletModule(theMetricRegistry));

    var resourceConfig = buildResourceConfig(theMetricRegistry);
    var jerseyConfiguration = buildConfiguration(new SystemPropertyProvider(), resourceConfig);
    bind(JerseyConfiguration.class).toInstance(jerseyConfiguration);

    var jerseyServer = constructJerseyServer(jerseyConfiguration);
    bind(JerseyServer.class).toInstance(jerseyServer);
  }

  ResourceConfig buildResourceConfig(MetricRegistry metricRegistry) {
    return new ResourceConfig()
      .register(new InstrumentedResourceMethodApplicationListener(metricRegistry));
  }

  JerseyConfiguration buildConfiguration(SystemPropertyProvider systemPropertyProvider, ResourceConfig resourceConfig) {
    int port = Integer.valueOf(systemPropertyProvider.getProperty("port", "8080"));
    return JerseyConfiguration.builder()
      .addPackage("justinb99.sampleapi.service.resource")
      .addPort(port)
      .registerClasses(ObjectMapperProvider.class)
      .registerClasses(JacksonFeature.class)
      .withResourceConfig(resourceConfig)
      .build();
  }

  private JerseyServer constructJerseyServer(JerseyConfiguration jerseyConfiguration) {
    var injectorProvider = getProvider(Injector.class);
    Supplier<Injector> injectorSupplier = injectorProvider::get;

    Constructor<JerseyServer> constructor;
    JerseyServer jerseyServer;

    try {
      constructor = JerseyServer.class.
        getDeclaredConstructor(JerseyConfiguration.class, Supplier.class);
      constructor.setAccessible(true);
      jerseyServer = constructor.newInstance(jerseyConfiguration, injectorSupplier);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return jerseyServer;
  }

}
