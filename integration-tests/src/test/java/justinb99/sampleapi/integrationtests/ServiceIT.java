package justinb99.sampleapi.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.logz.guice.jersey.JerseyServer;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import justinb99.sampleapi.engine.date.ISO8601DateParser;
import justinb99.sampleapi.schema.RateOuterClass;
import justinb99.sampleapi.service.Main;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static justinb99.sampleapi.schema.RateOuterClass.Rate.PRICE_FIELD_NUMBER;
import static justinb99.sampleapi.schema.RateOuterClass.Rate.STATUS_FIELD_NUMBER;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceIT {

  private static final String SERVICE_URL = "http://localhost:8080";
  private static final String RATE_URL = SERVICE_URL + "/v1/rate";

  //2018-07-18 was a Wednesday
  private static final String START = "2018-07-18T07:00:00Z";
  private static final String END = "2018-07-18T12:00:00Z";
  private static Integer EXPECTED_PRICE = 1750;

  //2018-07-20 was a Friday
  private static final String START_UNAVAILABLE = "2018-07-20T07:00:00Z";
  private static final String END_UNAVAILABLE = "2018-07-20T8:00:00Z";

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
  public void get_rate_available_same_start_end() {
    assertAvailableJsonRate(getRate(RATE_URL, START, START));
    assertAvailableJsonRate(getRate(RATE_URL, END, END));
  }

  @Test
  public void get_rate_unavailable() {
    getJsonRateUnavailable(RATE_URL);
  }

  @Test
  public void get_rate_unavailable_too_wide() {
    var start = "2018-07-18T00:00:00Z";
    var end = "2018-07-18T23:59:59Z";
    assertJsonRateUnavailable(start, end);
  }

  @Test
  public void get_rate_unavailable_inverted() {
    assertJsonRateUnavailable(END, START);
  }

  @Test
  public void get_rate_unavailable_multiple_days() {
    assertJsonRateUnavailable(END, START_UNAVAILABLE);
  }

  private void assertJsonRateUnavailable(String start, String end) {
    var validatableResponse = getRate(RATE_URL, start, end);
    assertJsonRateUnavailable(validatableResponse);
  }

  @Test
  public void get_rate_different_timezone() {
    //Tuesday @ 2:00:59.999 UTC
    var start = "2018-07-24T04:59:59.999+02:59";
    //Tuesday @ 6:23:00.123 UTC
    var end = "2018-07-24T05:00:00.123-0123";
    var validatableResponse = getRate(RATE_URL, start, end);
    assertAvailableJsonRate(validatableResponse, 925);
  }

  @Test
  public void get_rate_unavailable_bad_times() {
    assertJsonRateUnavailable(getRate(RATE_URL, START, "2018-7-18T1200"));
    assertJsonRateUnavailable(getRate(RATE_URL, "2018-7-18T700", END));
  }

  @Test
  public void get_rate_no_time_span() {
      given()
        .log().ifValidationFails()
        .get(RATE_URL)
        .then()
        .log().ifValidationFails()
        .statusCode(200)
        .body("status", equalTo("unavailable"));
  }

  @Test
  public void get_rate_just_start() {
    given()
      .log().ifValidationFails()
      .get(RATE_URL + "?start={start}", START)
      .then()
      .log().ifValidationFails()
      .statusCode(200)
      .body("status", equalTo("unavailable"));
  }

  @Test
  public void get_rate_just_end() {
    given()
      .log().ifValidationFails()
      .get(RATE_URL + "?end={end}", END)
      .then()
      .log().ifValidationFails()
      .statusCode(200)
      .body("status", equalTo("unavailable"));
  }

  @Test
  public void get_rate_json() {
    getJsonRate(RATE_URL + ".json");
  }

  @Test
  public void get_rate_json_unavailable() {
    getJsonRate(RATE_URL + ".json");
  }

  @Test
  public void get_rate_xml() {
    getRate(RATE_URL + ".xml")
      .contentType(ContentType.XML)
      .body("rate.price", equalTo(EXPECTED_PRICE.toString()));
  }

  @Test
  public void get_rate_xml_unavailable() {
    getRateUnavailable(RATE_URL + ".xml")
      .contentType(ContentType.XML)
      .body("rate.status", equalTo("unavailable"));
  }

  @Test
  public void get_rate_proto() throws Exception {
    var responseBytes = getRate(RATE_URL + ".proto")
      .contentType(ContentType.BINARY)
      .extract()
      .body()
      .asByteArray();

    var rate = RateOuterClass.Rate.parseFrom(responseBytes);
    var expected = RateOuterClass.Rate.newBuilder()
      .setPrice(EXPECTED_PRICE)
      .build();
    assertEquals(expected, rate);
    assertEquals(PRICE_FIELD_NUMBER, rate.getPriceOrStatusCase().getNumber());
  }

  @Test
  public void get_rate_proto_unavailable() throws Exception {
    var responseBytes = getRateUnavailable(RATE_URL + ".proto")
      .contentType(ContentType.BINARY)
      .extract()
      .body()
      .asByteArray();

    var rate = RateOuterClass.Rate.parseFrom(responseBytes);
    var expected = RateOuterClass.Rate.newBuilder()
      .setStatus(RateOuterClass.Rate.Status.unavailable)
      .build();
    assertEquals(expected, rate);
    assertEquals(STATUS_FIELD_NUMBER, rate.getPriceOrStatusCase().getNumber());
  }

  @Test
  public void get_documentation() {
    var docsIndex = given()
      .get(SERVICE_URL + "/docs/index.html?some=garbage")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .asString();

    assertTrue(docsIndex.contains("parking rate"));
  }

  @Test
  public void get_documentation_js() {
    var docsIndex = given()
      .get(SERVICE_URL + "/docs/javascripts/spectacle.js")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .asString();

    assertTrue(docsIndex.contains("Traverse"));
  }

  @Test
  public void get_documentation_404() {
    given()
      .get(SERVICE_URL + "/docs/doesnotexist.html")
      .then()
      .statusCode(404);
  }

  @Test
  public void get_ping() {
    var pong = given()
      .get(SERVICE_URL + "/ping")
      .then()
      .contentType("text/plain;charset=iso-8859-1")
      .statusCode(200)
      .extract()
      .body()
      .asString();

    assertEquals("pong\n", pong);
  }

  @Test
  public void get_metrics() {
    var metrics = given()
      .get(SERVICE_URL + "/metrics")
      .then()
      .contentType("application/json")
      .statusCode(200)
      .extract()
      .body()
      .asString();

    var numberOfEndpointsTimed = StringUtils.countMatches(metrics, "RateResource.getRate");
    assertEquals(4, numberOfEndpointsTimed);
  }

  private void getJsonRate(String url) {
    assertAvailableJsonRate(getRate(url));
  }

  private void assertAvailableJsonRate(ValidatableResponse validatableResponse) {
    assertAvailableJsonRate(validatableResponse, EXPECTED_PRICE);
  }

  private void assertAvailableJsonRate(
    ValidatableResponse validatableResponse,
    Integer expectedPrice
  ) {
    var response = validatableResponse
      .contentType(ContentType.JSON)
      .extract()
      .as(ObjectNode.class);

    var expected = createObjectNode();
    expected.put("price", expectedPrice);

    assertEquals(expected, response);
  }

  private void getJsonRateUnavailable(String url) {
    assertJsonRateUnavailable(getRateUnavailable(url));
  }

  private void assertJsonRateUnavailable(ValidatableResponse validatableResponse) {
    var response = validatableResponse.contentType(ContentType.JSON)
      .extract()
      .as(ObjectNode.class);

    var expected = createObjectNode();
    expected.put("status", "unavailable");

    assertEquals(expected, response);
  }

  private ValidatableResponse getRate(String url) {
    return getRate(url, START, END);
  }

  private ValidatableResponse getRateUnavailable(String url) {
    return getRate(url, START_UNAVAILABLE, END_UNAVAILABLE);
  }

  private ValidatableResponse getRate(String url, String start, String end) {
    return given()
      .log().ifValidationFails()
      .get(url + "?start={start}&end={end}", start, end)
      .then()
      .log().ifValidationFails()
      .statusCode(200);
  }

  private ObjectNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

}
