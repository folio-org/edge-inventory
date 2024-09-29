package org.folio.edge.inventory.handler;

import static org.folio.edge.inventory.TestConstants.INSTANCE_BY_ID_FORBIDDEN_URI;
import static org.folio.edge.inventory.TestConstants.INSTANCE_BY_ID_NOT_AUTHORIZED_URI;
import static org.folio.edge.inventory.TestConstants.INSTANCE_BY_ID_NOT_FOUND_URI;
import static org.folio.edge.inventory.TestConstants.LANG_PARAM_INVALID_VALUE;
import static org.folio.edge.inventory.TestConstants.LANG_PARAM_NAME;
import static org.folio.edge.inventory.TestConstants.LANG_PARAM_VALID_VALUE;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.inventory.BaseIntegrationTests;
import org.folio.edge.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class InventoryErrorHandlerTest extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private InventoryService inventoryService;

  @Test
  void handleForbiddenException_shouldProcessException() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_ID_FORBIDDEN_URI, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isForbidden())
        .andExpect(header()
            .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("code", is(403)));
  }

  @Test
  void handleNotFound_shouldProcessException() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_ID_NOT_FOUND_URI, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isNotFound())
        .andExpect(header()
            .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  void handleNotAuthorized_shouldProcessException() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_ID_NOT_AUTHORIZED_URI, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isUnauthorized())
        .andExpect(header()
            .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  void handleBadRequest_shouldProcessException() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_ID_NOT_AUTHORIZED_URI, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest())
        .andExpect(header()
            .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }

}
