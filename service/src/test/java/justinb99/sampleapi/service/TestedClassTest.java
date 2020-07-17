package justinb99.sampleapi.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestedClassTest {

  @Test
  public void getEvenOdd() {
    assertEquals(TestedClass.getEvenOdd(2), "even");
    assertEquals(TestedClass.getEvenOdd(3), "odd");
  }
}
