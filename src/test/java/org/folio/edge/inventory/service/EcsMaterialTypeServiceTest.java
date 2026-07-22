package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.TestConstants.CONSORTIA_TENANTS_RESPONSE_PATH;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_ID;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_ID_NOT_FOUND;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_NOT_FOUND_PATH;
import static org.folio.edge.inventory.TestConstants.MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_PATH;
import static org.folio.edge.inventory.TestConstants.USER_TENANTS_RESPONSE_PATH;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.ConsortiaClient;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.UserClient;
import org.folio.inventory.domain.dto.TenantCollection;
import org.folio.inventory.domain.dto.UserTenants;
import org.folio.spring.FolioExecutionContext;
import org.folio.spring.FolioModuleMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EcsMaterialTypeServiceTest {

  @Mock
  private ConsortiaClient consortiaClient;
  @Mock
  private UserClient usersClient;
  @Mock
  private InventoryClient inventoryClient;
  @Mock
  private FolioExecutionContext folioExecutionContext;
  @Mock
  private FolioModuleMetadata folioModuleMetadata;
  @Spy
  @InjectMocks
  private EcsMaterialTypeService ecsMaterialTypeService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    lenient().when(folioExecutionContext.getFolioModuleMetadata()).thenReturn(folioModuleMetadata);
    lenient().when(folioModuleMetadata.getModuleName()).thenReturn("edge-inventory");
    when(folioExecutionContext.getInstance()).thenReturn(folioExecutionContext);
  }

  @Test
  @SneakyThrows
  void getEcsMaterialTypeById_shouldReturnMaterialTypeFromMemberTenant() {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_RESPONSE_PATH),
        UserTenants.class);
    var consortiaTenants = objectMapper.readValue(
        TestUtil.readFileContentFromResources(CONSORTIA_TENANTS_RESPONSE_PATH), TenantCollection.class);
    var expectedMaterialTypeResponse = getJsonNodeByPath(MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_PATH);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    when(consortiaClient.getTenants(any())).thenReturn(consortiaTenants);
    when(inventoryClient.getMaterialTypeById(MATERIAL_TYPE_ID)).thenReturn(
        expectedMaterialTypeResponse);

    var materialTypeJson = objectMapper.readTree(ecsMaterialTypeService.getEcsMaterialTypeById(MATERIAL_TYPE_ID));

    assertTrue(materialTypeJson.hasNonNull("id"));
    assertTrue(materialTypeJson.hasNonNull("name"));
    assertTrue(materialTypeJson.hasNonNull("source"));
  }

  @Test
  @SneakyThrows
  void getEcsMaterialTypeById_shouldThrowEntityNotFound_whenMaterialTypeNotFoundInAllTenants() {
    var userTenants = objectMapper.readValue(TestUtil.readFileContentFromResources(USER_TENANTS_RESPONSE_PATH),
        UserTenants.class);
    var consortiaTenants = objectMapper.readValue(
        TestUtil.readFileContentFromResources(CONSORTIA_TENANTS_RESPONSE_PATH), TenantCollection.class);
    var errorResponse = getJsonNodeByPath(MATERIAL_TYPE_OF_MEMBER_FROM_CENTRAL_TENANT_NOT_FOUND_PATH);
    when(usersClient.getUserTenants()).thenReturn(userTenants);
    when(consortiaClient.getTenants(any())).thenReturn(consortiaTenants);
    when(inventoryClient.getMaterialTypeById(MATERIAL_TYPE_ID_NOT_FOUND)).thenReturn(
        errorResponse);

    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
        ecsMaterialTypeService.getEcsMaterialTypeById(MATERIAL_TYPE_ID_NOT_FOUND)
    );

    assertTrue(exception.getMessage().contains("Material type not found"));
  }

  private JsonNode getJsonNodeByPath(String resourcePath) {
    return objectMapper.readTree(TestUtil.readFileContentFromResources(resourcePath));
  }
}
