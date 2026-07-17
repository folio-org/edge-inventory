package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.TestConstants.CENTRAL_TEST_TENANT;
import static org.folio.edge.inventory.TestConstants.GET_VIEW_INSTANCES_WITHOUT_HOLDINGS_PATH;
import static org.folio.edge.inventory.TestConstants.GET_VIEW_INSTANCES_WITH_HOLDINGS_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDINGS_FACET_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDINGS_FACET_WITH_TWO_TENANTS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDINGS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDING_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.INSTANCE_SUMMARY_MEMBER_1_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.INSTANCE_SUMMARY_MEMBER_2_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.INSTANCE_SUMMARY_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.ITEMS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_NON_CONSORTIA_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.VALID_INSTANCE_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORDS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCES;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ITEMS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.folio.edge.inventory.TestConstants;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UserClient;
import org.folio.edge.inventory.service.mapper.RequestQueryParametersMapper;
import org.folio.inventory.domain.dto.FacetResponse;
import org.folio.inventory.domain.dto.HoldingResponse;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.folio.inventory.domain.dto.UserTenants;
import org.folio.spring.FolioExecutionContext;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EcsInventoryServiceTest {

  @Mock
  private UserClient usersClient;
  @Mock
  private InventoryClient inventoryClient;
  @Mock
  private SearchClient searchClient;
  @Mock
  private FolioExecutionContext folioExecutionContext;
  @Mock
  private RequestQueryParametersMapper requestQueryParametersMapper;

  @InjectMocks
  private EcsInventoryService ecsInventoryService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    var emptyQueryMap = Collections.emptyMap();
    lenient().doReturn(emptyQueryMap)
        .when(requestQueryParametersMapper).toMap(any(RequestQueryParameters.class));
  }

  @Test
  @SneakyThrows
  void getEcsInstanceSummary_shouldReturnMergedSummaryFromTenantsWithHoldings() {
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.getAllHeaders()).thenReturn(okapiHeaders(CENTRAL_TEST_TENANT));
    when(folioExecutionContext.getOkapiHeaders()).thenReturn(okapiHeaders(CENTRAL_TEST_TENANT));
    when(inventoryClient.getInstanceSummary(VALID_INSTANCE_ID))
        .thenReturn(getJsonNode(INSTANCE_SUMMARY_RESPONSE_PATH))
        .thenReturn(getJsonNode(INSTANCE_SUMMARY_MEMBER_1_RESPONSE_PATH))
        .thenReturn(getJsonNode(INSTANCE_SUMMARY_MEMBER_2_RESPONSE_PATH));
    var facetResponse = objectMapper.readValue(
        TestUtil.readFileContentFromResources(HOLDINGS_FACET_WITH_TWO_TENANTS_RESPONSE_PATH), FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);

    var response = objectMapper.readTree(
        ecsInventoryService.getEcsInstanceSummary(VALID_INSTANCE_ID, CENTRAL_TEST_TENANT));

    verify(inventoryClient, times(3)).getInstanceSummary(VALID_INSTANCE_ID);
    assertEquals("Central summary instance", response.get("instance").get("title").asString());
    assertTrue(response.get("isBoundWith").asBoolean());
    assertEquals(4, response.get("recordCounts").get("holdings").get("total").asInt());
    assertEquals(1, response.get("recordCounts").get("holdings").get("suppressedFromDiscovery").asInt());
    assertEquals(3, response.get("recordCounts").get("holdings").get("notSuppressedFromDiscovery").asInt());
    assertEquals(8, response.get("recordCounts").get("items").get("total").asInt());
    assertEquals(1, response.get("recordCounts").get("items").get("suppressedFromDiscovery").asInt());
    assertEquals(1, response.get("recordCounts").get("items").get("suppressedByHoldings").asInt());
    assertEquals(2, response.get("recordCounts").get("items").get("suppressedFromDiscoveryOrByHoldings").asInt());
    assertEquals(6, response.get("recordCounts").get("items").get("notSuppressedFromDiscovery").asInt());

    var allRecords = response.get("aggregates").get("allRecords");
    assertEquals("CN 001", allRecords.get("itemDerivedFields").get("effectiveShelvingOrder").asString());
    assertEquals(3, allRecords.get("electronicAccess").size());
    assertEquals("https://example.org/central", allRecords.get("electronicAccess").get(0).get("uri").asString());
    assertEquals("https://example.org/member-one", allRecords.get("electronicAccess").get(1).get("uri").asString());
    assertEquals("https://example.org/member-two", allRecords.get("electronicAccess").get(2).get("uri").asString());
    assertEquals(3, allRecords.get("referenceValues").get("itemMaterialTypes").size());
    assertEquals("book", allRecords.get("referenceValues").get("itemMaterialTypes").get(0).get("name").asString());
  }

  @Test
  @SneakyThrows
  void getEcsInstanceSummary_shouldFailWhenMemberTenantSummaryFails() {
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.getAllHeaders()).thenReturn(okapiHeaders(CENTRAL_TEST_TENANT));
    when(folioExecutionContext.getOkapiHeaders()).thenReturn(okapiHeaders(CENTRAL_TEST_TENANT));
    when(inventoryClient.getInstanceSummary(VALID_INSTANCE_ID))
        .thenReturn(getJsonNode(INSTANCE_SUMMARY_RESPONSE_PATH))
        .thenThrow(new IllegalStateException("member summary failed"));
    var facetResponse = objectMapper.readValue(
        TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH), FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);

    assertThrows(RuntimeException.class, () ->
        ecsInventoryService.getEcsInstanceSummary(VALID_INSTANCE_ID, CENTRAL_TEST_TENANT));

    verify(inventoryClient, times(2)).getInstanceSummary(VALID_INSTANCE_ID);
  }

  @Test
  @SneakyThrows
  void getEcsInventoryViewInstances_shouldReturnInventoryViewWithHoldingsFromMemberTenant() {
    var request = new RequestQueryParameters();
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    when(inventoryClient.getInventoryViewInstances(anyMap(), eq(false)))
        .thenReturn(getJsonNode(GET_VIEW_INSTANCES_WITHOUT_HOLDINGS_PATH));
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getInventoryViewInstances(anyMap(), eq(false), eq(TestConstants.TEST_TENANT)))
        .thenReturn(getJsonNode(GET_VIEW_INSTANCES_WITH_HOLDINGS_PATH));

    var response = objectMapper.readTree(ecsInventoryService.getEcsInventoryViewInstances(request, false));

    assertTrue(response.findValue(INSTANCES).isArray());
    assertTrue(response.findValue(INSTANCES).get(0).has(HOLDINGS_RECORDS));
    assertTrue(response.findValue(INSTANCES).get(0).has(ITEMS));
    assertTrue(response.findValue(INSTANCES).get(1).has(HOLDINGS_RECORDS));
    assertTrue(response.findValue(INSTANCES).get(1).has(ITEMS));
  }

  @Test
  @SneakyThrows
  void getEcsInventoryHoldings_shouldReturnInventoryHoldingsFromMemberTenant() {
    var request = new RequestQueryParameters();
    request.setQuery("instanceId==d41d9172-1869-11eb-adc1-0242ac120002%20not%20discoverySuppress==true");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getHoldings(anyMap(), eq(TestConstants.TEST_TENANT)))
        .thenReturn(getJsonNode(HOLDINGS_RESPONSE_PATH));

    var response = objectMapper.readTree(ecsInventoryService.getEcsInventoryHoldings(request));

    assertTrue(response.has(HOLDINGS_RECORDS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  @SneakyThrows
  void getEcsInventoryItems_shouldReturnInventoryHoldingsFromMemberTenant_queryWithInstanceId() {
    var request = new RequestQueryParameters();
    request.setQuery("instance.id==d41d9172-1869-11eb-adc1-0242ac120002");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getItems(anyMap(), eq(TestConstants.TEST_TENANT)))
        .thenReturn(getJsonNode(ITEMS_RESPONSE_PATH));

    var response = objectMapper.readTree(ecsInventoryService.getEcsInventoryItems(request));

    assertTrue(response.has(ITEMS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  @SneakyThrows
  void getEcsInventoryItems_shouldReturnInventoryHoldingsFromMemberTenant_queryWithHoldingsRecordId() {
    var request = new RequestQueryParameters();
    request.setQuery("cql.allRecords=1%20NOT%20discoverySuppress==true%20and%20holdingsRecordId==(e3ff6133-b9a2-4d4c-a1c9-dc1867d4df19)");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var holdingResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDING_RESPONSE_PATH),
        HoldingResponse.class);
    when(searchClient.getConsortiumHolding(any())).thenReturn(holdingResponse);
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(eq(ecsInventoryService.FACET), anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getItems(anyMap(), eq(TestConstants.TEST_TENANT)))
        .thenReturn(getJsonNode(ITEMS_RESPONSE_PATH));

    var response = objectMapper.readTree(ecsInventoryService.getEcsInventoryItems(request));

    assertTrue(response.has(ITEMS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  @SneakyThrows
  void isCentralTenant_shouldReturnTrueWhenTenantIsCentral() {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_RESPONSE_PATH), UserTenants.class);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    var isCentral = ecsInventoryService.isCentralTenant(CENTRAL_TEST_TENANT);
    assertTrue(isCentral);
  }

  @Test
  @SneakyThrows
  void isCentralTenant_shouldReturnFalseWhenNonConsortia() {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_NON_CONSORTIA_RESPONSE_PATH), UserTenants.class);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    var isCentral = ecsInventoryService.isCentralTenant(TestConstants.TEST_TENANT);
    assertFalse(isCentral);
  }

  private JsonNode getJsonNode(String resourcePath) {
    return objectMapper.readTree(TestUtil.readFileContentFromResources(resourcePath));
  }

  private Map<String, Collection<String>> okapiHeaders(String tenantId) {
    var headers = new HashMap<String, Collection<String>>();
    headers.put(XOkapiHeaders.TENANT, List.of(tenantId));
    headers.put(XOkapiHeaders.URL, List.of("http://localhost"));
    headers.put(XOkapiHeaders.TOKEN, List.of("token"));
    return headers;
  }
}
