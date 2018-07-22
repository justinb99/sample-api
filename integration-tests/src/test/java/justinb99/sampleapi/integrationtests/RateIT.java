package justinb99.sampleapi.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import io.logz.guice.jersey.JerseyServer;
import justinb99.sampleapi.service.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class RateIT {

  private static final String RATE_URL = "http://localhost:8080/rate";

  private static final String START = "2015-07-01T07:00:00Z";
  private static final String END = "2015-07-01T12:00:00Z";

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
    getJsonRate(RATE_URL);
  }

  @Test
  public void get_rate_json() {
    getJsonRate(RATE_URL + ".json");
  }

  @Test
  public void get_rate_xml() {
    var response = getRate(RATE_URL + ".xml")
      .contentType(ContentType.XML)
      .extract()
      .body().asString();

    System.out.println(response);

//    var expected = createObjectNode();
//    expected.put("price", 1500);

//    assertEquals(expected, response);
  }

  private void getJsonRate(String url) {
    var response = getRate(url)
      .contentType(ContentType.JSON)
      .extract()
      .as(ObjectNode.class);

    var expected = createObjectNode();
    expected.put("price", 1500);

    assertEquals(expected, response);
  }

  private ValidatableResponse getRate(String url) {
    return given()
      .log().ifValidationFails()
      .get(url + "?start={start}&end={end}", START, END)
      .then()
      .log().ifValidationFails()
      .statusCode(200);
  }

  private ObjectNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

}
