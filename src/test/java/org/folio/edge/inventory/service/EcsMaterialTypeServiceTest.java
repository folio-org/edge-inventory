package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.TestConstants.CENTRAL_TEST_TENANT;
import static org.folio.edge.inventory.TestConstants.CONSORTIA_TENANTS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_ID;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_RESPONSE_PATH;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.ConsortiaClient;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.UsersClient;
import org.folio.edge.inventory.util.JsonConverter;
import org.folio.inventory.domain.dto.TenantCollection;
import org.folio.inventory.domain.dto.UserTenants;
import org.folio.spring.FolioExecutionContext;
import org.folio.spring.FolioModuleMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EcsMaterialTypeServiceTest {

  @Mock
  private ConsortiaClient consortiaClient;
  @Mock
  private UsersClient usersClient;
  @Mock
  private InventoryClient inventoryClient;
  @Mock
  private FolioExecutionContext folioExecutionContext;
  @Mock
  private FolioModuleMetadata folioModuleMetadata;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final JsonConverter jsonConverter = new JsonConverter(objectMapper);
  private EcsMaterialTypeService ecsMaterialTypeService;

  @BeforeEach
  void setUp() {
    when(folioExecutionContext.getFolioModuleMetadata()).thenReturn(folioModuleMetadata);
    when(folioModuleMetadata.getModuleName()).thenReturn("edge-inventory");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
    when(folioExecutionContext.execute(any())).thenCallRealMethod();

    ecsMaterialTypeService = new EcsMaterialTypeService(usersClient, consortiaClient, inventoryClient, jsonConverter,
        folioExecutionContext);
  }

  @Test
  void getEcsMaterialTypeById_shouldReturnMaterialTypeFromMemberTenant()
      throws JsonProcessingException {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_RESPONSE_PATH),
        UserTenants.class);
    var consortiaTenants = objectMapper.readValue(
        TestUtil.readFileContentFromResources(CONSORTIA_TENANTS_RESPONSE_PATH), TenantCollection.class);
    var expectedMaterialTypeResponse = TestUtil.readFileContentFromResources(
        MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_PATH);

    when(usersClient.getUserTenants()).thenReturn(userTenants);
    when(consortiaClient.getTenants(any())).thenReturn(consortiaTenants);
    when(inventoryClient.getMaterialTypeById(MATERIAL_TYPE_ID, CENTRAL_TEST_TENANT)).thenReturn(
        expectedMaterialTypeResponse);

    var materialTypeJson = jsonConverter.readAsTree(ecsMaterialTypeService.getEcsMaterialTypeById(MATERIAL_TYPE_ID));

    assertTrue(materialTypeJson.hasNonNull("id"));
    assertTrue(materialTypeJson.hasNonNull("name"));
    assertTrue(materialTypeJson.hasNonNull("source"));
  }


}
