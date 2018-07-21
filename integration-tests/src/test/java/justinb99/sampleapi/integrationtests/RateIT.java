package justinb99.sampleapi.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.restassured.http.ContentType;
import io.logz.guice.jersey.JerseyServer;
import justinb99.sampleapi.service.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class RateIT {

  private static final String RATE_URL = "http://localhost:8080/rate";
  private static JerseyServer server;
  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void beforeClass() throws Exception {
    server = Main.startServer();
    objectMapper = new ObjectMapper();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    server.stop();
  }

  @Test
  public void get_rate() {
    var start = "2015-07-01T07:00:00Z";
    var end = "2015-07-01T12:00:00Z";

    var response = given()
      .get(RATE_URL + "?start={start}&end={end}", start, end)
      .then()
      .log().ifValidationFails()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .extract()
      .as(ObjectNode.class);

    var expected = createObjectNode();
    expected.put("price", 1500);

    assertEquals(expected, response);
  }

  private ObjectNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

}
