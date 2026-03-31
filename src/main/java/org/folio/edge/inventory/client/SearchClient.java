package org.folio.edge.inventory.client;

import org.folio.inventory.domain.dto.FacetResponse;
import org.folio.inventory.domain.dto.HoldingResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import tools.jackson.databind.JsonNode;

@HttpExchange(url = "search", contentType = "application/json")
public interface SearchClient {

  @GetExchange(value = "/instances/facets", accept = "application/json")
  FacetResponse getInstanceFacet(@RequestParam String facet, @RequestParam String query);

  @GetExchange("/consortium/locations")
  JsonNode getConsortiumLocations(@RequestParam String id);

  @GetExchange("/consortium/libraries")
  JsonNode getConsortiumLibraries(@RequestParam String id);

  @GetExchange("/consortium/institutions")
  JsonNode getConsortiumInstitutions(@RequestParam String id);

  @GetExchange("/consortium/campuses")
  JsonNode getConsortiumCampuses(@RequestParam String id);

  @GetExchange("/consortium/holding/{id}")
  HoldingResponse getConsortiumHolding(@PathVariable("id") String id);

}
