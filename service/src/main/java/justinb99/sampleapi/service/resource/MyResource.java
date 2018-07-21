package justinb99.sampleapi.service.resource;

import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

  private final String injectedResponse;

  @Inject
  public MyResource(@Named("InjectedResponse") String injectedResponse) {
    this.injectedResponse = injectedResponse;
    LoggerFactory.getLogger(getClass()).info("INSTANTIATING MYRESOURCE. injectedResponse={}", injectedResponse);
  }

  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getIt() {
    return injectedResponse;
  }
}
