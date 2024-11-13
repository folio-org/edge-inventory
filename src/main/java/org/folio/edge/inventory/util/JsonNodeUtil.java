package org.folio.edge.inventory.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonNodeUtil {

  public static JsonNode findNodeInArrayWith(ArrayNode node, String fieldName, String value) {
    var elements = node.elements();
    while (elements.hasNext()) {
      var jsonNode = elements.next();
      if (jsonNode.has(fieldName) && jsonNode.findValue(fieldName).asText().equals(value)) {
        return jsonNode;
      }
    }
    return null;
   }

}
