package justinb99.sampleapi.service.inject;

import com.google.inject.AbstractModule;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.glassfish.jersey.jackson.JacksonFeature;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    JerseyConfiguration configuration = buildConfiguration();
    install(new JerseyModule(configuration));
  }

  JerseyConfiguration buildConfiguration() {
    int port = Integer.valueOf(System.getProperty("port", "8080"));
    return JerseyConfiguration.builder()
      .addPackage("justinb99.sampleapi.service.resource")
      .addPort(port)
      .registerClasses(ObjectMapperProvider.class)
      .registerClasses(JacksonFeature.class)
      .build();
  }

}
