package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORDS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORD_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCES;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCE_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ITEMS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;
import static org.folio.spring.utils.FolioExecutionContextUtils.prepareContextForTenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class EcsInventoryService {

  public static final String FACET = "holdings.tenantId";
  private static final String AGGREGATES = "aggregates";
  private static final String ALL_RECORDS = "allRecords";
  private static final String ELECTRONIC_ACCESS = "electronicAccess";
  private static final String EFFECTIVE_SHELVING_ORDER = "effectiveShelvingOrder";
  private static final String ID_FIELD = "id";
  private static final String IS_BOUND_WITH = "isBoundWith";
  private static final String ITEM_DERIVED_FIELDS = "itemDerivedFields";
  private static final String ITEM_MATERIAL_TYPES = "itemMaterialTypes";
  private static final String NAME = "name";
  private static final String NOT_SUPPRESSED_FROM_DISCOVERY_RECORDS = "notSuppressedFromDiscoveryRecords";
  private static final String RECORD_COUNTS = "recordCounts";
  private static final String REFERENCE_VALUES = "referenceValues";
  private static final String SUMMARY_HOLDINGS = "holdings";
  private static final String SUPPRESSED_BY_HOLDINGS = "suppressedByHoldings";
  private static final String SUPPRESSED_FROM_DISCOVERY = "suppressedFromDiscovery";
  private static final String SUPPRESSED_FROM_DISCOVERY_OR_BY_HOLDINGS = "suppressedFromDiscoveryOrByHoldings";
  private static final String NOT_SUPPRESSED_FROM_DISCOVERY = "notSuppressedFromDiscovery";
  private static final String SUMMARY_TOTAL = "total";
  private static final String URI = "uri";
  private static final Pattern HOLDINGS_ID_PATTERN = Pattern.compile(HOLDINGS_RECORD_ID + "==\\(([^)]+)\\)");
  private static final Pattern INSTANCE_ID_PATTERN = Pattern.compile("(instance(?:\\.id|Id))==([a-fA-F0-9\\-]{36})");
  private final UserClient userClient;
  private final InventoryClient inventoryClient;
  private final SearchClient searchClient;
  private final FolioExecutionContext folioExecutionContext;
  private final RequestQueryParametersMapper requestQueryParametersMapper;

  public boolean isCentralTenant(String tenantId) {
    var effectiveTenantId = effectiveTenantId(tenantId);
    var userTenants = userClient.getUserTenants();
    if (userTenants.getTotalRecords() > 0) {
      return effectiveTenantId != null && effectiveTenantId.equals(userTenants.getUserTenants().get(0).getCentralTenantId());
    }
    return false;
  }

  public String getEcsInstanceSummary(String instanceId, String currentTenantId) {
    var centralSummary = inventoryClient.getInstanceSummary(instanceId);
    var effectiveTenantId = effectiveTenantId(currentTenantId);
    var memberTenantIds = getInstanceTenants(instanceId).tenantIds().stream()
        .filter(tenantId -> !Objects.equals(tenantId, effectiveTenantId))
        .toList();
    if (memberTenantIds.isEmpty()) {
      return centralSummary.toString();
    }

    getInstanceSummaries(instanceId, memberTenantIds)
        .forEach(memberSummary -> mergeInstanceSummary(centralSummary, memberSummary));
    return centralSummary.toString();
  }

  private String effectiveTenantId(String tenantId) {
    var contextTenantId = folioExecutionContext.getTenantId();
    return contextTenantId == null ? tenantId : contextTenantId;
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
        .forEach((instance) -> instanceIds.add(instance.get(INSTANCE_ID).asString()));
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

  private List<JsonNode> getInstanceSummaries(String instanceId, List<String> tenantIds) {
    var context = (FolioExecutionContext) folioExecutionContext.getInstance();
    return tenantIds.parallelStream()
        .map(tenantId -> prepareContextForTenant(tenantId, context.getFolioModuleMetadata(), context)
            .execute(() -> inventoryClient.getInstanceSummary(instanceId)))
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

  private void mergeInstanceSummary(JsonNode target, JsonNode source) {
    mergeBoundWith(target, source);
    mergeRecordCounts(target, source);
    mergeAggregateScope(target, source, ALL_RECORDS);
    mergeAggregateScope(target, source, NOT_SUPPRESSED_FROM_DISCOVERY_RECORDS);
  }

  private void mergeBoundWith(JsonNode target, JsonNode source) {
    if (target instanceof ObjectNode targetObject) {
      targetObject.put(IS_BOUND_WITH, booleanValue(target, IS_BOUND_WITH) || booleanValue(source, IS_BOUND_WITH));
    }
  }

  private void mergeRecordCounts(JsonNode target, JsonNode source) {
    addCount(target, source, SUMMARY_HOLDINGS, SUMMARY_TOTAL);
    addCount(target, source, SUMMARY_HOLDINGS, SUPPRESSED_FROM_DISCOVERY);
    addCount(target, source, SUMMARY_HOLDINGS, NOT_SUPPRESSED_FROM_DISCOVERY);
    addCount(target, source, ITEMS, SUMMARY_TOTAL);
    addCount(target, source, ITEMS, SUPPRESSED_FROM_DISCOVERY);
    addCount(target, source, ITEMS, SUPPRESSED_BY_HOLDINGS);
    addCount(target, source, ITEMS, SUPPRESSED_FROM_DISCOVERY_OR_BY_HOLDINGS);
    addCount(target, source, ITEMS, NOT_SUPPRESSED_FROM_DISCOVERY);
  }

  private void addCount(JsonNode target, JsonNode source, String recordType, String fieldName) {
    var targetCounts = target.withObject("/" + RECORD_COUNTS + "/" + recordType);
    var sourceCounts = getNode(source, RECORD_COUNTS, recordType);
    targetCounts.put(fieldName, intValue(targetCounts, fieldName) + intValue(sourceCounts, fieldName));
  }

  private void mergeAggregateScope(JsonNode target, JsonNode source, String scope) {
    Comparator<JsonNode> namedValueComparator = Comparator
        .comparing((JsonNode node) -> stringValue(node, NAME), Comparator.nullsLast(String::compareTo))
        .thenComparing(this::namedValueKey);
    mergeEffectiveShelvingOrder(target, source, scope);
    mergeArray(target, source, "/" + AGGREGATES + "/" + scope, ELECTRONIC_ACCESS,
        this::electronicAccessKey, null);
    mergeArray(target, source, "/" + AGGREGATES + "/" + scope + "/" + REFERENCE_VALUES, ITEM_MATERIAL_TYPES,
        this::namedValueKey, namedValueComparator);
  }

  private void mergeEffectiveShelvingOrder(JsonNode target, JsonNode source, String scope) {
    var targetFields = target.withObject("/" + AGGREGATES + "/" + scope + "/" + ITEM_DERIVED_FIELDS);
    var targetValue = stringValue(targetFields, EFFECTIVE_SHELVING_ORDER);
    var sourceValue = stringValue(getNode(source, AGGREGATES, scope, ITEM_DERIVED_FIELDS), EFFECTIVE_SHELVING_ORDER);
    var mergedValue = firstShelvingOrder(targetValue, sourceValue);
    if (mergedValue != null) {
      targetFields.put(EFFECTIVE_SHELVING_ORDER, mergedValue);
    }
  }

  private String firstShelvingOrder(String value1, String value2) {
    if (value1 == null) {
      return value2;
    }
    if (value2 == null) {
      return value1;
    }
    return value1.compareTo(value2) <= 0 ? value1 : value2;
  }

  private void mergeArray(JsonNode target, JsonNode source, String parentPointer, String arrayName,
      Function<JsonNode, String> keyProvider, Comparator<JsonNode> comparator) {
    var merged = new LinkedHashMap<String, JsonNode>();
    addArrayValues(merged, getNode(target.withObject(parentPointer), arrayName), keyProvider);
    addArrayValues(merged, getNode(source, path(parentPointer, arrayName)), keyProvider);

    var values = new ArrayList<>(merged.values());
    if (comparator != null) {
      values.sort(comparator);
    }

    var mergedArray = JsonNodeFactory.instance.arrayNode();
    values.forEach(mergedArray::add);
    target.withObject(parentPointer).set(arrayName, mergedArray);
  }

  private void addArrayValues(Map<String, JsonNode> valuesByKey, JsonNode arrayNode,
      Function<JsonNode, String> keyProvider) {
    if (!(arrayNode instanceof ArrayNode values)) {
      return;
    }
    for (JsonNode value : values.elements()) {
      valuesByKey.putIfAbsent(keyProvider.apply(value), value);
    }
  }

  private String electronicAccessKey(JsonNode electronicAccess) {
    var uri = stringValue(electronicAccess, URI);
    return uri == null ? electronicAccess.toString() : uri;
  }

  private String namedValueKey(JsonNode namedValue) {
    var id = stringValue(namedValue, ID_FIELD);
    return id == null ? namedValue.toString() : id;
  }

  private String[] path(String parentPointer, String fieldName) {
    var path = parentPointer.substring(1) + "/" + fieldName;
    return path.split("/");
  }

  private JsonNode getNode(JsonNode node, String... fieldNames) {
    var current = node;
    for (String fieldName : fieldNames) {
      if (current == null) {
        return null;
      }
      current = current.get(fieldName);
    }
    return current;
  }

  private int intValue(JsonNode node, String fieldName) {
    var value = node == null ? null : node.get(fieldName);
    return value == null ? 0 : value.asInt();
  }

  private boolean booleanValue(JsonNode node, String fieldName) {
    var value = node == null ? null : node.get(fieldName);
    return value != null && value.asBoolean();
  }

  private String stringValue(JsonNode node, String fieldName) {
    var value = node == null ? null : node.get(fieldName);
    if (value == null || value.isNull()) {
      return null;
    }
    var stringValue = value.asString();
    return stringValue == null || stringValue.isBlank() ? null : stringValue;
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
      var ids = insideParentheses.split(" or ");
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
