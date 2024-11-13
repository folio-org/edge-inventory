package org.folio.edge.inventory.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.inventory.BaseIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class TenantControllerIT extends BaseIntegrationTests {

  @Autowired 
  private MockMvc mockMvc;

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
