package justinb99.sampleapi.engine.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CollectionUtilTest {

  @Test
  public void toSet() {
    var set = CollectionUtil.toSet(9, 8, 7);
    var expectedSet = Set.of(7, 8, 9);
    assertEquals(expectedSet, set);
  }

}
