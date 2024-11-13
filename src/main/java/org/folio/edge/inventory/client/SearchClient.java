package org.folio.edge.inventory.client;

import org.folio.edge.inventory.config.InventoryClientConfig;
import org.folio.inventory.domain.dto.FacetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "search", configuration = InventoryClientConfig.class)
public interface SearchClient {

  @GetMapping(value = "/search/instances/facets", consumes = MediaType.APPLICATION_JSON_VALUE)
  FacetResponse getInstanceFacet(@RequestParam String facet, @RequestParam String query);

  @GetMapping(value = "/search/consortium/locations")
  String getConsortiumLocations(@RequestParam String id);

  @GetMapping(value = "/search/consortium/libraries")
  String getConsortiumLibraries(@RequestParam String id);

  @GetMapping(value = "/search/consortium/institutions")
  String getConsortiumInstitutions(@RequestParam String id);

  @GetMapping(value = "/search/consortium/campuses")
  String getConsortiumCampuses(@RequestParam String id);

}
