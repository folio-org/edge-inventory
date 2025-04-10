package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORDS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCES;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCE_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ITEMS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UsersClient;
import org.folio.edge.inventory.models.InstanceTenants;
import org.folio.edge.inventory.util.JsonConverter;
import org.folio.edge.inventory.util.JsonNodeUtil;
import org.folio.edge.inventory.util.QueryUtil;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.folio.spring.FolioExecutionContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcsInventoryService {

  public static final String FACET = "holdings.tenantId";
  private final UsersClient usersClient;
  private final InventoryClient inventoryClient;
  private final SearchClient searchClient;
  private final JsonConverter jsonConverter;
  private final FolioExecutionContext folioExecutionContext;

  public boolean isCentralTenant(String tenantId) {
    var userTenants = usersClient.getUserTenants();
    if (userTenants.getTotalRecords() > 0) {
      return tenantId.equals(userTenants.getUserTenants().get(0).getCentralTenantId());
    }
    return false;
  }

  public String getEcsInventoryViewInstances(RequestQueryParameters requestQueryParameters, Boolean withBoundedItems) {
    JsonNode centralView = jsonConverter.readAsTree(
        inventoryClient.getInventoryViewInstances(requestQueryParameters, withBoundedItems));
    var totalRecords = centralView.get(TOTAL_RECORDS).asInt();
    if (totalRecords < 0) {
      return centralView.toString();
    }
    var instanceIds = getInstanceIdsFromView(centralView);
    var holdingsTenantsMap = getHoldingsTenantsMap(instanceIds);
    var tenantInstanceViews = getHoldingsAndItemsFromMemberTenants(holdingsTenantsMap, withBoundedItems);
    tenantInstanceViews.forEach(instanceView -> mergeInstanceViews(centralView, instanceView));
    return centralView.toString();
  }

  private List<String> getInstanceIdsFromView(JsonNode instanceView) {
    var instanceIds = new ArrayList<String>();
    instanceView.withArray(INSTANCES).elements()
        .forEachRemaining((instance) -> instanceIds.add(instance.get(INSTANCE_ID).asText()));
    return instanceIds;
  }

  private Map<String, List<String>> getHoldingsTenantsMap(List<String> instanceIds) {
    var tenantInstanceMap = new HashMap<String, List<String>>();
    var instanceTenants = getInstanceTenants(instanceIds);
    instanceTenants.forEach(instanceTenant -> instanceTenant.tenantIds()
        .forEach(
            tenantId -> {
              tenantInstanceMap.putIfAbsent(tenantId, new ArrayList<>());
              tenantInstanceMap.get(tenantId).add(instanceTenant.instanceId());
            }));
    return tenantInstanceMap;
  }

  private List<JsonNode> getHoldingsAndItemsFromMemberTenants(Map<String, List<String>> tenantInstanceMap,
      Boolean withBoundedItems) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return tenantInstanceMap.entrySet().parallelStream()
        .map(tenantInstances -> context.execute(() -> jsonConverter
            .readAsTree(inventoryClient.getInventoryViewInstances(
                getRequestQueryParams(tenantInstances.getValue()), withBoundedItems, tenantInstances.getKey()))))
        .toList();
  }

  private void mergeInstanceViews(JsonNode instanceView1, JsonNode instanceView2) {
    var instances1 = instanceView1.withArrayProperty(INSTANCES);
    var instances2 = instanceView2.withArrayProperty(INSTANCES);
    var instance2Elements = instances2.elements();
    while (instance2Elements.hasNext()) {
      var instance2 = instance2Elements.next();
      var instance1 = JsonNodeUtil.findNodeInArrayWith(instances1, INSTANCE_ID, instance2.get(INSTANCE_ID).asText());
      instance1.withArrayProperty(HOLDINGS_RECORDS).addAll(instance2.withArrayProperty(HOLDINGS_RECORDS));
      instance1.withArrayProperty(ITEMS).addAll(instance2.withArrayProperty(ITEMS));
    }
  }

  private RequestQueryParameters getRequestQueryParams(List<String> instanceIds) {
    return new RequestQueryParameters().query(QueryUtil.exactMatchAny(instanceIds, ID)).limit(instanceIds.size());
  }

  private List<InstanceTenants> getInstanceTenants(List<String> instanceIds) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return instanceIds.parallelStream()
        .map(instanceId -> context.execute(() -> {
          var response = searchClient.getInstanceFacet(FACET, "id==" + instanceId);
          if (response.getFacets().getHoldingsTenantId().getTotalRecords() == 0) {
            return new InstanceTenants(instanceId, Collections.EMPTY_LIST);
          }
          var tenantIds = response.getFacets().getHoldingsTenantId().getValues()
              .stream().map(value -> value.getId()).toList();
          return new InstanceTenants(instanceId, tenantIds);
        })).toList();
  }
}
