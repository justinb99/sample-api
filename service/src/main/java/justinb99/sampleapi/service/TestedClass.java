package justinb99.sampleapi.service;

public class TestedClass {

  private TestedClass() {}

  public static String getEvenOdd(int value) {
    if (value % 2 == 0)
      return "even";
    else
      return "odd";
  }
}
