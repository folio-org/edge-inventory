package org.folio.edge.inventory.controller;

import static org.folio.edge.inventory.TestConstants.CAMPUS_ID;
import static org.folio.edge.inventory.TestConstants.GET_CAMPUS_BY_ID_URL;
import static org.folio.edge.inventory.TestConstants.GET_INSTITUTION_BY_ID_URL;
import static org.folio.edge.inventory.TestConstants.GET_LIBRARY_BY_ID_URL;
import static org.folio.edge.inventory.TestConstants.GET_LOCATION_BY_ID_URL;
import static org.folio.edge.inventory.TestConstants.GET_VIEW_INSTANCES_URL;
import static org.folio.edge.inventory.TestConstants.INSTITUTION_ID;
import static org.folio.edge.inventory.TestConstants.LIBRARY_ID;
import static org.folio.edge.inventory.TestConstants.LOCATION_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.inventory.BaseIntegrationTests;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(OrderAnnotation.class)
class EcsInventoryControllerIT extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getInventoryViewInstances_shouldReturnInventoryViewInstancesWithHoldingsFromMemberTenant() throws Exception {
    doGetWithParams(mockMvc, GET_VIEW_INSTANCES_URL, "query", "ecs", true)
        .andExpect(status().isOk())
        .andExpect(jsonPath("instances[0].instanceId", is("f1e82a1e-fc06-4b82-bb1d-da326cb378ce")))
        .andExpect(jsonPath("instances[0].isBoundWith", is(false)))
        .andExpect(jsonPath("instances[0].items", hasSize(1)))
        .andExpect(jsonPath("instances[0].holdingsRecords", hasSize(1)))
        .andExpect(jsonPath("instances[1].instanceId", is("31fcc8e7-a019-43f4-b642-2edc389f4501")))
        .andExpect(jsonPath("instances[1].isBoundWith", is(false)))
        .andExpect(jsonPath("instances[1].items", hasSize(1)))
        .andExpect(jsonPath("instances[1].holdingsRecords", hasSize(1)))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getLocationById_shouldReturnLocationFromSearchWhenCentral() throws Exception {
    doGet(mockMvc, GET_LOCATION_BY_ID_URL, true)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(LOCATION_ID)))
        .andExpect(jsonPath("name", is("Annex")))
        .andExpect(jsonPath("code", is("KU/CC/DI/A")));
  }

  @Test
  void getCampusById_shouldReturnLocationFromSearchWhenCentral() throws Exception {
    doGet(mockMvc, GET_CAMPUS_BY_ID_URL, true)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(CAMPUS_ID)))
        .andExpect(jsonPath("name", is("Annex")))
        .andExpect(jsonPath("code", is("KU/CC/DI/A")));
  }

  @Test
  void getInstitutionById_shouldReturnLocationFromSearchWhenCentral() throws Exception {
    doGet(mockMvc, GET_INSTITUTION_BY_ID_URL, true)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(INSTITUTION_ID)))
        .andExpect(jsonPath("name", is("Annex")))
        .andExpect(jsonPath("code", is("KU/CC/DI/A")));
  }

  @Test
  void getLibraryById_shouldReturnLocationFromSearchWhenCentral() throws Exception {
    doGet(mockMvc, GET_LIBRARY_BY_ID_URL, true)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(LIBRARY_ID)))
        .andExpect(jsonPath("name", is("Annex")))
        .andExpect(jsonPath("code", is("KU/CC/DI/A")));
  }


}
