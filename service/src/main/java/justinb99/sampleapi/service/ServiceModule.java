package justinb99.sampleapi.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;

public class ServiceModule extends AbstractModule {

  @Provides
  @Named("InjectedResponse")
  public String provideInjectedResponse() {
    return "Hello World!";
  }

}
