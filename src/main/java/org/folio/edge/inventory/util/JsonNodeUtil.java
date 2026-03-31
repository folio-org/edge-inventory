package org.folio.edge.inventory.util;

import lombok.experimental.UtilityClass;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

@UtilityClass
public class JsonNodeUtil {

  public static JsonNode findNodeInArrayWith(ArrayNode node, String fieldName, String value) {
    for (JsonNode jsonNode : node.elements()) {
      if (jsonNode.has(fieldName) && jsonNode.findValue(fieldName).asString().equals(value)) {
        return jsonNode;
      }
    }
    return null;
   }

}
