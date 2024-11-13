package org.folio.edge.inventory.util;

import static java.util.stream.Collectors.joining;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class QueryUtil {

  public static String exactMatchAny(List<String> values, String fieldName) {
    if (null == values || values.isEmpty()) {
      return StringUtils.EMPTY;
    }
    var valuesConcatenated = values.stream()
        .filter(StringUtils::isNotBlank)
        .collect(joining(" or "));
    return String.format("%s==(%s)", fieldName, valuesConcatenated);
  }

}