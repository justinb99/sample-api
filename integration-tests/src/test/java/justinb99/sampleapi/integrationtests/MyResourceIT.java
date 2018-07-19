package justinb99.sampleapi.integrationtests;

import justinb99.sampleapi.service.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class MyResourceIT {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Main.startServer())
    // --
    // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

    target = c.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdownNow();
  }

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  @Test
  public void testGetIt() {
    String responseMsg = target.path("myresource").request().get(String.class);
    assertEquals("Got it!", responseMsg);
  }

  @Test
  public void getMyResource() {
    String response = given()
      .get(Main.BASE_URI + "myresource")
      .then()
      .log().ifValidationFails()
      .statusCode(200)
      .extract()
      .body().asString();
    assertEquals("Got it!", response);
  }
}
