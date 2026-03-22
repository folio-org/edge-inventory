package org.folio.edge.inventory.client;

import org.folio.inventory.domain.dto.UserTenants;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface UserClient {

  @GetExchange("user-tenants")
  UserTenants getUserTenants();

}
