package org.folio.edge.inventory.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.inventory.BaseIntegrationTests;
import org.junit.jupiter.api.Test;


class TenantControllerIT extends BaseIntegrationTests {

  @Test
  void postTenant_shouldReturn204() throws Exception {
    mockMvc
        .perform(
            post("/_/tenant")
                .headers(defaultHeaders(false))
                .content("{ \"module_from\":  \"edge-inventory.1\",  \"module_to\":  \"edge-inventory.2\" }"))
        .andExpect(status().isNoContent());
  }
}
