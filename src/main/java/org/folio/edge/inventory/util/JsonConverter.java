package org.folio.edge.inventory.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
