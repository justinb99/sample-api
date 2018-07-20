package justinb99.sampleapi.engine.util;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class CollectionUtilTest {

  @Test
  public void toSet() {
    var set = CollectionUtil.toSet(9, 8, 7);
    var expectedSet = new HashSet<Integer>();
    expectedSet.add(7);
    expectedSet.add(8);
    expectedSet.add(9);
    assertEquals(expectedSet, set);
  }

}
