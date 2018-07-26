package justinb99.sampleapi.engine;

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
import java.util.Optional;

import static justinb99.sampleapi.schema.RateOuterClass.Rate.STATUS_FIELD_NUMBER;
import static justinb99.sampleapi.schema.RateOuterClass.Rate.Status.unavailable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateEngineTest {

  @Mock
  private ISO8601DateParser dateParser;
  @Mock
  private Rate rate1_isAvailable, rate2_notAvailable, rate3_isAvailable;
  private DateTimeRange dateTimeRange;
  private List<Rate> rates;
  private RateEngine target;
  private RateOuterClass.Rate unavailableRate;

  @Before
  public void before() {
    dateTimeRange = new DateTimeRange();

    when(rate1_isAvailable.isAvailable(same(dateTimeRange))).thenReturn(true);
    when(rate2_notAvailable.isAvailable(same(dateTimeRange))).thenReturn(false);
    when(rate3_isAvailable.isAvailable(same(dateTimeRange))).thenReturn(true);

    rates = new LinkedList<>();
    target = new RateEngine(rates, dateParser);

    unavailableRate = RateOuterClass.Rate.newBuilder()
      .setStatus(RateOuterClass.Rate.Status.unavailable).build();
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

  @Test
  public void getUnavailableRate() {
    var unavailable = target.getUnavailableRate();
    assertEquals(unavailableRate, unavailable);
    assertEquals(STATUS_FIELD_NUMBER, unavailable.getPriceOrStatusCase().getNumber());
  }

  @Test
  public void getRate_complete() {
    var start = "start";
    var end = "end";
    var expectedRate = RateOuterClass.Rate.newBuilder().build();
    rates.add(rate1_isAvailable);

    when(dateParser.parseRange(start, end)).thenReturn(Optional.of(dateTimeRange));
    when(rate1_isAvailable.asAvailablePbRate()).thenReturn(expectedRate);

    var rate = target.getRate(start, end);
    assertSame(expectedRate, rate);

    verify(dateParser).parseRange(start, end);
    verify(rate1_isAvailable).asAvailablePbRate();
  }

  @Test
  public void getRate_unavailable() {
    var start = "start";
    var end = "end";
    DateTimeRange alternateRange = new DateTimeRange();

    rates.add(rate1_isAvailable);
    when(dateParser.parseRange(start, end)).thenReturn(Optional.of(alternateRange));

    var rate = target.getRate(start, end);
    assertEquals(unavailableRate, rate);

    verify(dateParser).parseRange(start, end);
    verify(rate1_isAvailable).isAvailable(same(alternateRange));
  }

  @Test
  public void getRate_parse_failure() {
    var start = "start";
    var end = "end";

    rates.add(rate1_isAvailable);
    when(dateParser.parseRange(start, end)).thenReturn(Optional.empty());

    var rate = target.getRate(start, end);
    assertEquals(unavailableRate, rate);

    verify(dateParser).parseRange(start, end);
    verify(rate1_isAvailable, never()).isAvailable(any());
  }




}
