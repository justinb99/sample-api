package justinb99.sampleapi.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyServer;
import justinb99.sampleapi.engine.inject.EngineModule;
import justinb99.sampleapi.service.inject.ServiceModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    logger.info("Starting Rate server...");

    var server = startServer();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        logger.info("Stopping Rate server...");
        server.stop();
        logger.info("Rate server stopped!");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }));
  }

  public static JerseyServer startServer() throws Exception {
    return startServer(createInjector());
  }

  static Injector createInjector() {
    return Guice.createInjector(
      new EngineModule(),
      new ServiceModule()
    );
  }

  static JerseyServer startServer(Injector injector) throws Exception {
    var server = injector.getInstance(JerseyServer.class);
    server.start();
    logger.info("Rate server started!");
    return server;
  }

}
