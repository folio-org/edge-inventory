package org.folio.edge.inventory.client;

import org.folio.edge.inventory.config.InventoryClientConfig;
import org.folio.inventory.domain.dto.TenantCollection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "consortia", configuration = InventoryClientConfig.class)
public interface ConsortiaClient {

  @GetMapping(value = "/{consortiumId}/tenants", consumes = MediaType.APPLICATION_JSON_VALUE)
  TenantCollection getTenants(@PathVariable String consortiumId);
}
