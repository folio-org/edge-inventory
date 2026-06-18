package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORDS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORD_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCES;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCE_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ITEMS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UserClient;
import org.folio.edge.inventory.models.InstanceTenants;
import org.folio.edge.inventory.service.mapper.RequestQueryParametersMapper;
import org.folio.edge.inventory.util.JsonNodeUtil;
import org.folio.edge.inventory.util.QueryUtil;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.folio.spring.FolioExecutionContext;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Service
@RequiredArgsConstructor
public class EcsInventoryService {

  public static final String FACET = "holdings.tenantId";
  private static final Pattern HOLDINGS_ID_PATTERN = Pattern.compile(HOLDINGS_RECORD_ID + "==\\(([^)]+)\\)");
  private static final Pattern INSTANCE_ID_PATTERN = Pattern.compile("(instance(?:\\.id|Id))==([a-fA-F0-9\\-]{36})");
  private static final Pattern HOLDINGS_ID_SEPARATOR_PATTERN = Pattern.compile(" or ");
  private final UserClient userClient;
  private final InventoryClient inventoryClient;
  private final SearchClient searchClient;
  private final FolioExecutionContext folioExecutionContext;
  private final RequestQueryParametersMapper requestQueryParametersMapper;

  public boolean isCentralTenant(String tenantId) {
    var userTenants = userClient.getUserTenants();
    if (userTenants.getTotalRecords() > 0) {
      return tenantId.equals(userTenants.getUserTenants().get(0).getCentralTenantId());
    }
    return false;
  }

  public String getEcsInventoryViewInstances(RequestQueryParameters requestQueryParameters, Boolean withBoundedItems) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    JsonNode centralView = inventoryClient.getInventoryViewInstances(requestQueryParametersMap, withBoundedItems);
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
        .forEach(instance -> instanceIds.add(instance.get(INSTANCE_ID).asString()));
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
        .map(tenantInstances -> context.execute(() -> {
          var requestQueryParametersMap = requestQueryParametersMapper.toMap(
              getRequestQueryParams(tenantInstances.getValue()));
          return inventoryClient.getInventoryViewInstances(
              requestQueryParametersMap, withBoundedItems, tenantInstances.getKey());
        }))
        .toList();
  }

  private void mergeInstanceViews(JsonNode instanceView1, JsonNode instanceView2) {
    var instances1 = instanceView1.withArrayProperty(INSTANCES);
    var instances2 = instanceView2.withArrayProperty(INSTANCES);
    var instance2Elements = instances2.elements();
    for (JsonNode instance2 : instance2Elements) {
      var instance1 = JsonNodeUtil.findNodeInArrayWith(instances1, INSTANCE_ID, instance2.get(INSTANCE_ID).asString());
      if (instance1 != null) {
        instance1.withArrayProperty(HOLDINGS_RECORDS).addAll(instance2.withArrayProperty(HOLDINGS_RECORDS));
      }
      if (instance1 != null) {
        instance1.withArrayProperty(ITEMS).addAll(instance2.withArrayProperty(ITEMS));
      }
    }
  }

  private RequestQueryParameters getRequestQueryParams(List<String> instanceIds) {
    return new RequestQueryParameters().query(QueryUtil.exactMatchAny(instanceIds, ID)).limit(instanceIds.size());
  }

  private List<InstanceTenants> getInstanceTenants(List<String> instanceIds) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return instanceIds.parallelStream().map(id -> context.execute(() -> getInstanceTenants(id))).toList();
  }

  private InstanceTenants getInstanceTenants(String instanceId) {
    var response = searchClient.getInstanceFacet(FACET, "id==" + instanceId);
    if (response.getFacets().getHoldingsTenantId().getTotalRecords() == 0) {
      return new InstanceTenants(instanceId, Collections.<String>emptyList());
    }
    var tenantIds = response.getFacets().getHoldingsTenantId().getValues()
        .stream().map(value -> value.getId()).toList();
    return new InstanceTenants(instanceId, tenantIds);
  }

  public String getEcsInventoryHoldings(RequestQueryParameters requestQueryParameters) {
    var query = requestQueryParameters == null ? null : requestQueryParameters.getQuery();
    var instanceId = parseInstanceId(query);
    if (instanceId == null) {
      return flattenHoldings(List.of());
    }
    var instanceTenants = getInstanceTenants(instanceId);
    var tenantHoldings = getHoldings(instanceTenants, requestQueryParameters);
    return flattenHoldings(tenantHoldings);
  }

  private List<JsonNode> getHoldings(InstanceTenants instanceTenants, RequestQueryParameters requestQueryParameters) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return instanceTenants.tenantIds().parallelStream()
        .map(tenantId -> context.execute(() -> {
          var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
          return inventoryClient.getHoldings(requestQueryParametersMap, tenantId);
        }))
        .toList();
  }

  private String flattenHoldings(List<JsonNode> tenantHoldings) {
    var response = JsonNodeFactory.instance.objectNode();
    var mergedHoldings = JsonNodeFactory.instance.arrayNode();
    int totalRecordsSum = 0;
    if (tenantHoldings == null || tenantHoldings.isEmpty()) {
      response.set(HOLDINGS_RECORDS, mergedHoldings);
      response.put(TOTAL_RECORDS, totalRecordsSum);
      return response.toString();
    }

    for (JsonNode node : tenantHoldings) {
      mergedHoldings.addAll(node.withArrayProperty(HOLDINGS_RECORDS));
      var totalRecordsNode = node.get(TOTAL_RECORDS);
      if (totalRecordsNode != null && totalRecordsNode.isInt()) {
        totalRecordsSum += totalRecordsNode.intValue();
      }
    }

    response.set(HOLDINGS_RECORDS, mergedHoldings);
    response.put(TOTAL_RECORDS, totalRecordsSum);
    return response.toString();
  }

  public String getEcsInventoryItems(RequestQueryParameters requestQueryParameters) {
    var query = requestQueryParameters == null ? null : requestQueryParameters.getQuery();
    String instanceId;
    if (query != null && query.contains(HOLDINGS_RECORD_ID)) {
      var holdingId = parseHoldingId(query);
      if (holdingId == null) {
        return flattenItems(List.of());
      }
      var holdingResponse = searchClient.getConsortiumHolding(holdingId);
      instanceId = holdingResponse.getInstanceId();
    } else {
      instanceId = parseInstanceId(query);
    }

    if (instanceId == null) {
      return flattenItems(List.of());
    }

    var instanceTenants = getInstanceTenants(instanceId);
    var tenantItems = getItems(instanceTenants, requestQueryParameters);
    return flattenItems(tenantItems);
  }

  private String flattenItems(List<JsonNode> tenantItems) {
    var response = JsonNodeFactory.instance.objectNode();
    var mergedItems = JsonNodeFactory.instance.arrayNode();
    int totalRecordsSum = 0;
    if (tenantItems == null || tenantItems.isEmpty()) {
      response.set(ITEMS, mergedItems);
      response.put(TOTAL_RECORDS, totalRecordsSum);
      return response.toString();
    }

    for (JsonNode node : tenantItems) {
      mergedItems.addAll(node.withArrayProperty(ITEMS));
      var totalRecordsNode = node.get(TOTAL_RECORDS);
      if (totalRecordsNode != null && totalRecordsNode.isInt()) {
        totalRecordsSum += totalRecordsNode.intValue();
      }
    }

    response.set(ITEMS, mergedItems);
    response.put(TOTAL_RECORDS, totalRecordsSum);
    return response.toString();
  }

  private List<JsonNode> getItems(InstanceTenants instanceTenants, RequestQueryParameters requestQueryParameters) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return instanceTenants.tenantIds().parallelStream()
        .map(tenantId -> context.execute(() -> {
          var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
          return inventoryClient.getItems(requestQueryParametersMap, tenantId);
        }))
        .toList();
  }

  private String parseHoldingId(String query) {
    if (query == null || query.isBlank()) {
      return null;
    }
    var matcher = HOLDINGS_ID_PATTERN.matcher(query);
    if (matcher.find()) {
      var insideParentheses = matcher.group(1);
      var ids = HOLDINGS_ID_SEPARATOR_PATTERN.split(insideParentheses);
      if (ids.length > 0) {
        return ids[0].trim();
      }
    }
    return null;
  }

  private String parseInstanceId(String query) {
    if (query == null || query.isBlank()) {
      return null;
    }
    var matcher = INSTANCE_ID_PATTERN.matcher(query);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return null;
  }
}
