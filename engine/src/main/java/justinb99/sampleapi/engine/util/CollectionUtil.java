package justinb99.sampleapi.engine.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtil {

  @SafeVarargs
  public static <T> Set<T> toSet(T... items) {
    return new HashSet<>(Arrays.asList(items));
  }

}
