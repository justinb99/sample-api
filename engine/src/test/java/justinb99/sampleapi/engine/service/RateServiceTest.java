package justinb99.sampleapi.engine.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import justinb99.sampleapi.engine.date.ISO8601DateParser;
import justinb99.sampleapi.engine.model.DateTimeRange;
import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.schema.RateOuterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

  @Mock
  private ISO8601DateParser iso8601DateParser;
  @Mock
  private Rate rate1_isAvailable, rate2_notAvailable, rate3_isAvailable;
  private DateTimeRange dateTimeRange;
  private List<Rate> rates;
  private RateService target;

  @Before
  public void before() {
    dateTimeRange = new DateTimeRange();

    when(rate1_isAvailable.isAvailable(same(dateTimeRange))).thenReturn(true);
    when(rate2_notAvailable.isAvailable(same(dateTimeRange))).thenReturn(false);
    when(rate3_isAvailable.isAvailable(same(dateTimeRange))).thenReturn(true);

    rates = new LinkedList<>();
    target = new RateService(rates, iso8601DateParser);
  }

  @Test
  public void pb() throws Exception {
    var rate = RateOuterClass.Rate.newBuilder()
      .setPrice(1500)
      .build();

    var writer = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .registerModule(new ProtobufModule())
      .writerFor(RateOuterClass.Rate.class)
      .withDefaultPrettyPrinter();

    var json = writer.writeValueAsString(rate);
    System.out.println(json);

    rate = RateOuterClass.Rate.newBuilder()
      .setStatus(RateOuterClass.Rate.Status.unavailable)
      .build();
    json = writer.writeValueAsString(rate);
    System.out.println(json);
  }

  @Test
  public void getRate_empty() {
    var rate = target.getRate(dateTimeRange);
    assertFalse(rate.isPresent());
  }

  @Test
  public void getRate_1() {
    rates.add(rate1_isAvailable);
    var actual = target.getRate(dateTimeRange).get();
    assertSame(rate1_isAvailable, actual);
  }

  @Test
  public void getRate_2() {
    rates.add(rate2_notAvailable);
    rates.add(rate1_isAvailable);

    var actual = target.getRate(dateTimeRange).get();
    assertSame(rate1_isAvailable, actual);
  }

  @Test
  public void getRate_muliple_available() {
    rates.add(rate1_isAvailable);
    rates.add(rate2_notAvailable);
    rates.add(rate3_isAvailable);

    var actual = target.getRate(dateTimeRange).get();
    assertSame(rate1_isAvailable, actual);

    rates.clear();
    rates.add(rate3_isAvailable);
    rates.add(rate2_notAvailable);
    rates.add(rate1_isAvailable);

    actual = target.getRate(dateTimeRange).get();
    assertSame(rate3_isAvailable, actual);
  }


}
