package org.folio.edge.inventory.client;

import org.folio.inventory.domain.dto.TenantCollection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "consortia", contentType = "application/json")
public interface ConsortiaClient {

  @GetExchange("/{consortiumId}/tenants")
  TenantCollection getTenants(@PathVariable("consortiumId") String consortiumId);
}