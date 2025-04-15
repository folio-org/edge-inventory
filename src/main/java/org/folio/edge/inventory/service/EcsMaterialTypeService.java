package org.folio.edge.inventory.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.edge.inventory.client.ConsortiaClient;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.UsersClient;
import org.folio.edge.inventory.util.JsonConverter;
import org.folio.inventory.domain.dto.TenantCollection;
import org.folio.inventory.domain.dto.UserTenantsUserTenantsInner;
import org.folio.spring.FolioExecutionContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EcsMaterialTypeService {

  public static final String NAME = "name";

  private final UsersClient usersClient;
  private final ConsortiaClient consortiaClient;
  private final InventoryClient inventoryClient;
  private final JsonConverter jsonConverter;
  private final FolioExecutionContext folioExecutionContext;

  public String getEcsMaterialTypeById(String materialTypeId) {
    var userTenants = usersClient.getUserTenants();

    log.info("usersClient.getUserTenants() - {}", userTenants); //just for debugging

    if (userTenants != null) {
      var consortiumId = userTenants.getUserTenants().stream().map(
          UserTenantsUserTenantsInner::getConsortiumId
      ).findFirst();

      TenantCollection tenantCollection = consortiumId
          .map(consortiaClient::getTenants)
          .orElseThrow(() -> {
            log.error("Consortium ID not found in user tenants");
            return new EntityNotFoundException("Consortium ID not found");
          });

      log.info("tenantCollection - {}", tenantCollection); //just for debugging

      var materialTypes = getMaterialTypesFromMemberTenants(tenantCollection, materialTypeId);
      return materialTypes.stream()
          .filter(json -> json.has(NAME))
          .findFirst()
          .map(JsonNode::toString)
          .orElseGet(() -> {
            log.error("Material type not found, returning respective response body");
            return materialTypes.getFirst().toString();
          });
    }

    log.error("User tenants not found");
    throw new EntityNotFoundException("User tenants not found");
  }

  private List<JsonNode> getMaterialTypesFromMemberTenants(TenantCollection tenantCollection, String materialTypeId) {
    var instance = (FolioExecutionContext) folioExecutionContext.getInstance();
    return tenantCollection.getTenants().stream().map(
        tenant -> instance.execute(
            () -> jsonConverter.readAsTree(inventoryClient.getMaterialTypeById(materialTypeId, tenant.getName()))
        )).toList();
  }

}
