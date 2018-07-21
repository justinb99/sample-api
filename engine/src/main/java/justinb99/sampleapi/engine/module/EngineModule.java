package justinb99.sampleapi.engine.module;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class EngineModule extends AbstractModule {

  @Provides @Singleton
  public ObjectMapper provideObjectMapper() {
    return new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL);
  }

}
