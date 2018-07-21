package justinb99.sampleapi.integrationtests;

import io.logz.guice.jersey.JerseyServer;
import justinb99.sampleapi.service.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class MyResourceIT {

  private static JerseyServer server;

  @BeforeClass
  public static void beforeClass() throws Exception {
    server = Main.startServer();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    server.stop();
  }

  @Test
  public void getMyResource() {
    String response = given()
      .get("http://localhost:8080/myresource")
      .then()
      .log().ifValidationFails()
      .statusCode(200)
      .extract()
      .body().asString();
    assertEquals("Hello World!", response);
  }

}
