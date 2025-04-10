package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.TestConstants.CENTRAL_TEST_TENANT;
import static org.folio.edge.inventory.TestConstants.GET_VIEW_INSTANCES_WITHOUT_HOLDINGS_PATH;
import static org.folio.edge.inventory.TestConstants.GET_VIEW_INSTANCES_WITH_HOLDINGS_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDINGS_FACET_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDINGS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.HOLDING_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.ITEMS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_NON_CONSORTIA_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.VALID_INSTANCE_ID;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.HOLDINGS_RECORDS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.INSTANCES;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.ITEMS;
import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.folio.edge.inventory.TestConstants;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UsersClient;
import org.folio.edge.inventory.util.JsonConverter;
import org.folio.edge.inventory.util.QueryUtil;
import org.folio.inventory.domain.dto.FacetResponse;
import org.folio.inventory.domain.dto.HoldingResponse;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.folio.inventory.domain.dto.UserTenants;
import org.folio.spring.FolioExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EcsInventoryServiceTest {

  @Mock
  private UsersClient usersClient;
  @Mock
  private InventoryClient inventoryClient;
  @Mock
  private SearchClient searchClient;
  @Mock
  private FolioExecutionContext folioExecutionContext;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final JsonConverter jsonConverter = new JsonConverter(objectMapper);
  private EcsInventoryService ecsInventoryService;

  @BeforeEach
  void setUp() {
    ecsInventoryService = new EcsInventoryService(usersClient, inventoryClient, searchClient, jsonConverter, folioExecutionContext);
  }

  @Test
  void getEcsInventoryViewInstances_shouldReturnInventoryViewWithHoldingsFromMemberTenant()
      throws JsonProcessingException {
    var request = new RequestQueryParameters();
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    when(inventoryClient.getInventoryViewInstances(request, false))
        .thenReturn(TestUtil.readFileContentFromResources(GET_VIEW_INSTANCES_WITHOUT_HOLDINGS_PATH));
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(Mockito.eq(ecsInventoryService.FACET), Mockito.anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getInventoryViewInstances(Mockito.any(), Mockito.eq(false), Mockito.eq(TestConstants.TEST_TENANT)))
        .thenReturn(TestUtil.readFileContentFromResources(GET_VIEW_INSTANCES_WITH_HOLDINGS_PATH));

    var response = jsonConverter.readAsTree(ecsInventoryService.getEcsInventoryViewInstances(request, false));
    assertTrue(response.findValue(INSTANCES).isArray());
    assertTrue(response.findValue(INSTANCES).get(0).has(HOLDINGS_RECORDS));
    assertTrue(response.findValue(INSTANCES).get(0).has(ITEMS));
    assertTrue(response.findValue(INSTANCES).get(1).has(HOLDINGS_RECORDS));
    assertTrue(response.findValue(INSTANCES).get(1).has(ITEMS));
  }

  @Test
  void getEcsInventoryHoldings_shouldReturnInventoryHoldingsFromMemberTenant() throws JsonProcessingException {
    var request = new RequestQueryParameters();
    request.setQuery("instanceId==d41d9172-1869-11eb-adc1-0242ac120002%20not%20discoverySuppress==true");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(Mockito.eq(ecsInventoryService.FACET), Mockito.anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getHoldings(Mockito.any(), Mockito.eq(TestConstants.TEST_TENANT)))
        .thenReturn(TestUtil.readFileContentFromResources(HOLDINGS_RESPONSE_PATH));

    var response = jsonConverter.readAsTree(ecsInventoryService.getEcsInventoryHoldings(request));
    assertTrue(response.has(HOLDINGS_RECORDS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  void getEcsInventoryItems_shouldReturnInventoryHoldingsFromMemberTenant_queryWithInstanceId() throws JsonProcessingException {
    var request = new RequestQueryParameters();
    request.setQuery("instance.id==d41d9172-1869-11eb-adc1-0242ac120002");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(Mockito.eq(ecsInventoryService.FACET), Mockito.anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getItems(Mockito.any(), Mockito.eq(TestConstants.TEST_TENANT)))
        .thenReturn(TestUtil.readFileContentFromResources(ITEMS_RESPONSE_PATH));

    var response = jsonConverter.readAsTree(ecsInventoryService.getEcsInventoryItems(request));
    assertTrue(response.has(ITEMS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  void getEcsInventoryItems_shouldReturnInventoryHoldingsFromMemberTenant_queryWithHoldingsRecordId() throws JsonProcessingException {
    var request = new RequestQueryParameters();
    request.setQuery("cql.allRecords=1%20NOT%20discoverySuppress==true%20and%20holdingsRecordId==(e3ff6133-b9a2-4d4c-a1c9-dc1867d4df19)");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();
    var holdingResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDING_RESPONSE_PATH),
        HoldingResponse.class);
    when(searchClient.getConsortiumHolding(Mockito.any())).thenReturn(holdingResponse);
    var facetResponse = objectMapper.readValue(TestUtil.readFileContentFromResources(HOLDINGS_FACET_RESPONSE_PATH),
        FacetResponse.class);
    when(searchClient.getInstanceFacet(Mockito.eq(ecsInventoryService.FACET), Mockito.anyString()))
        .thenReturn(facetResponse);
    when(inventoryClient.getItems(Mockito.any(), Mockito.eq(TestConstants.TEST_TENANT)))
        .thenReturn(TestUtil.readFileContentFromResources(ITEMS_RESPONSE_PATH));

    var response = jsonConverter.readAsTree(ecsInventoryService.getEcsInventoryItems(request));
    assertTrue(response.has(ITEMS));
    assertTrue(response.has(TOTAL_RECORDS));
  }

  @Test
  void isCentralTenant_shouldReturnTrueWhenTenantIsCentral() throws JsonProcessingException {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_RESPONSE_PATH), UserTenants.class);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    var isCentral = ecsInventoryService.isCentralTenant(CENTRAL_TEST_TENANT);
    assertTrue(isCentral);
  }

  @Test
  void isCentralTenant_shouldReturnFalseWhenNonConsortia() throws JsonProcessingException {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_NON_CONSORTIA_RESPONSE_PATH), UserTenants.class);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    var isCentral = ecsInventoryService.isCentralTenant(TestConstants.TEST_TENANT);
    assertFalse(isCentral);
  }

}
