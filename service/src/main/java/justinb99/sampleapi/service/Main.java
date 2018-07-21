package justinb99.sampleapi.service;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Main class.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static JerseyServer startServer() throws Exception {
    var configuration = JerseyConfiguration.builder()
      .addPackage("justinb99.sampleapi.service.resource")
      .addPort(8080)
      .build();

    var modules = new ArrayList<Module>();
    modules.add(new JerseyModule(configuration));
    modules.add(new ServiceModule());
    //TODO: add more modules

    var server = Guice.createInjector(modules)
      .getInstance(JerseyServer.class);

    server.start();

    return server;
  }

  public static void main(String[] args) throws Exception {
    var server = startServer();
    logger.info("Jersey app started.  Hit enter to stop it...");
    System.in.read();
    server.stop();
  }

}

