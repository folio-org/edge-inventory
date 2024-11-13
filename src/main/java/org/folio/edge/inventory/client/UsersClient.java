package org.folio.edge.inventory.client;

import org.folio.edge.inventory.config.InventoryClientConfig;
import org.folio.inventory.domain.dto.UserTenants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "users", configuration = InventoryClientConfig.class)
public interface UsersClient {

  @GetMapping(value = "/user-tenants", consumes = MediaType.APPLICATION_JSON_VALUE)
  UserTenants getUserTenants();

}
