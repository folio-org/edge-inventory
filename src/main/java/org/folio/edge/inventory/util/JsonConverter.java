package org.folio.edge.inventory.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Log4j2
public class JsonConverter {
  private final ObjectMapper objectMapper;

  public JsonNode readAsTree(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (JacksonException e) {
      log.error("Exception occurred during json parsing", e);
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Failed to parse json");
    }
  }
}
