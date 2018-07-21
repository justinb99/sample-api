package justinb99.sampleapi.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Main class.
 */
public class Main {
  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8080/myapp/";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in justinb99.sampleapi package
    final ResourceConfig rc = new ResourceConfig().packages("justinb99.sampleapi");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws Exception {
    var logger = LoggerFactory.getLogger(Main.class);

    var configuration = JerseyConfiguration.builder()
      .addResourceClass(MyResource.class)
//      .addPackage("justinb99.sampleapi.service")
      .addPort(8080)
      .build();

    logger.info("\n\n\n*******contextPath='" + configuration.getContextPath() + "'\n\n\n");

    var modules = new ArrayList<Module>();
    modules.add(new JerseyModule(configuration));
    modules.add(new ServiceModule());
    //TODO: add more modules

    JerseyServer server = Guice.createInjector(modules)
      .getInstance(JerseyServer.class);

    server.start();
    logger.info("Jersey app started.  Hit enter to stop it...");
    System.in.read();
    server.stop();
  }
}

