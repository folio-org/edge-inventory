package org.folio.edge.inventory.service;

import static org.folio.edge.inventory.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String CODE = "code";
  private static final String ORDERING = "ordering";
  private static final String SOURCE = "source";
  private static final String TOTAL_RECORDS = "totalRecords";
  private static final String EFFECTIVE_SHELVING_ORDER = "effectiveShelvingOrder";

  @InjectMocks
  private InventoryService inventoryService;
  @Mock
  private InventoryClient inventoryClient;

  @Test
  void getInstanceById_shouldReturnInstance() throws JSONException {
    String expectedInventoryContent = TestUtil.readFileContentFromResources(INSTANCE_RESPONSE_PATH);
    when(inventoryClient
        .getInstance(VALID_INSTANCE_ID, LANG_PARAM_VALID_VALUE))
        .thenReturn(expectedInventoryContent);

    JSONObject actualInstance = new JSONObject(inventoryService.getInstance(VALID_INSTANCE_ID, LANG_PARAM_VALID_VALUE));

    assertEquals("e6bc03c6-c137-4221-b679-a7c5c31f986c", actualInstance.get(ID));
    assertEquals("inst000000000027", actualInstance.get("hrid"));
    assertEquals("Organisations- und Prozessentwicklung Harald Augustin (Hrsg.)", actualInstance.get("title"));
    assertEquals("FOLIO", actualInstance.get(SOURCE));
  }

  @Test
  void getAuthorityById_shouldReturnAuthority() throws JSONException {
    String expectedInventoryContent = TestUtil.readFileContentFromResources(AUTHORITY_RESPONSE_PATH);
    when(inventoryClient
            .getAuthority(VALID_AUTHORITY_ID))
            .thenReturn(expectedInventoryContent);

    JSONObject actualAuthority = new JSONObject(inventoryService.getAuthority(VALID_AUTHORITY_ID));

    assertEquals("9eba0866-8195-457c-ac72-dbfc279cc496", actualAuthority.get(ID));
    assertEquals("n88234700410821", actualAuthority.get("naturalId"));
    assertEquals("Woodson, Jacqueline C410821", actualAuthority.get("personalName"));
    assertEquals("MARC", actualAuthority.get(SOURCE));
  }

  @Test
  void getHoldings_shouldReturnHoldings() throws JSONException {
    String expectedHoldings = TestUtil.readFileContentFromResources(HOLDINGS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getHoldings(requestQueryParameters))
        .thenReturn(expectedHoldings);

    JSONObject holdings = new JSONObject(inventoryService.getHoldings(requestQueryParameters));
    JSONObject actualHoldongs = holdings.getJSONArray("holdingsRecords").getJSONObject(0);

    assertEquals(2, holdings.get(TOTAL_RECORDS));
    assertEquals("67cd0046-e4f1-4e4f-9024-adf0b0039d09", actualHoldongs.get(ID));
    assertEquals("hold000000000007", actualHoldongs.get("hrid"));
    assertEquals("D15.H63 A3 2002", actualHoldongs.get("callNumber"));
  }

  @Test
  void getIdentifierTypes_shouldReturnIdentifierTypes() throws JSONException {
    String expectedIdentifierTypesContent = TestUtil.readFileContentFromResources(IDENTIFIER_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getIdentifierTypes(requestQueryParameters))
        .thenReturn(expectedIdentifierTypesContent);

    JSONObject identifierTypes = new JSONObject(inventoryService.getIdentifierTypes(requestQueryParameters))
        .getJSONArray("identifierTypes").getJSONObject(0);

    assertEquals("7f907515-a1bf-4513-8a38-92e1a07c539d", identifierTypes.get(ID));
    assertEquals("ASIN", identifierTypes.get(NAME));
    assertEquals("folio", identifierTypes.get(SOURCE));
  }

  @Test
  void getClassificationTypes_shouldReturnClassificationTypes() throws JSONException {
    String expectedClassificationTypesContent = TestUtil.readFileContentFromResources(CLASSIFICATION_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getClassificationTypes(requestQueryParameters))
        .thenReturn(expectedClassificationTypesContent);

    JSONObject classificationTypes = new JSONObject(inventoryService.getClassificationTypes(requestQueryParameters))
        .getJSONArray("classificationTypes").getJSONObject(0);

    assertEquals("ad615f6e-e28c-4343-b4a0-457397c5be3e", classificationTypes.get(ID));
    assertEquals("Canadian Classification", classificationTypes.get(NAME));
    assertEquals("consortium", classificationTypes.get(SOURCE));
  }

  @Test
  void getContributorTypes_shouldReturnContributorTypes() throws JSONException {
    String expectedContributorTypesContent = TestUtil.readFileContentFromResources(CONTRIBUTOR_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getContributorTypes(requestQueryParameters))
        .thenReturn(expectedContributorTypesContent);

    JSONObject contributorTypes = new JSONObject(inventoryService.getContributorTypes(requestQueryParameters))
        .getJSONArray("contributorTypes").getJSONObject(0);

    assertEquals("7131e7b8-84fa-48bd-a725-14050be38f9f", contributorTypes.get(ID));
    assertEquals("Actor", contributorTypes.get(NAME));
    assertEquals("act", contributorTypes.get(CODE));
    assertEquals("consortium", contributorTypes.get(SOURCE));
  }

  @Test
  void getContributorNameTypes_shouldReturnContributorNameTypes() throws JSONException {
    String expectedContent = TestUtil.readFileContentFromResources(CONTRIBUTOR_NAME_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient.getContributorNameTypes(requestQueryParameters)).thenReturn(expectedContent);

    JSONObject contributorNameType = new JSONObject(inventoryService.getContributorNameTypes(requestQueryParameters))
        .getJSONArray("contributorNameTypes").getJSONObject(0);

    assertEquals("2e48e713-17f3-4c13-a9f8-23845bb210aa", contributorNameType.get(ID));
    assertEquals("Corporate name", contributorNameType.get(NAME));
    assertEquals("2", contributorNameType.get(ORDERING));
  }

  @Test
  void getLocations_shouldReturnLocations() throws JSONException {
    String expectedLocationsContent = TestUtil.readFileContentFromResources(LOCATIONS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getLocations(requestQueryParameters))
        .thenReturn(expectedLocationsContent);

    JSONObject locations = new JSONObject(inventoryService.getLocations(requestQueryParameters))
        .getJSONArray("locations").getJSONObject(0);

    assertEquals("b241764c-1466-4e1d-a028-1a3684a5da87", locations.get(ID));
    assertEquals("Popular Reading Collection", locations.get(NAME));
    assertEquals("KU/CC/DI/P", locations.get(CODE));
  }

  @Test
  void getServicePoints_shouldReturnServicePoints() throws JSONException {
    String expectedServicePointsContent = TestUtil.readFileContentFromResources(SERVICE_POINTS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getServicePoints(requestQueryParameters))
        .thenReturn(expectedServicePointsContent);

    JSONObject servicePoints = new JSONObject(inventoryService.getServicePoints(requestQueryParameters))
        .getJSONArray("servicepoints").getJSONObject(0);

    assertEquals("7c5abc9f-f3d7-4856-b8d7-6712462ca007", servicePoints.get(ID));
    assertEquals("Online", servicePoints.get(NAME));
    assertEquals("Online", servicePoints.get(CODE));
  }

  @Test
  void getNatureOfContentTerms_shouldReturnNatureOfContentTerms() throws JSONException {
    String expectedNatureOfContentTerms = TestUtil.readFileContentFromResources(NATURE_OF_CONTENT_TERMS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getNatureOfContentTerms(requestQueryParameters))
        .thenReturn(expectedNatureOfContentTerms);

    JSONObject natureOfContentTermsResponse = new JSONObject(
        inventoryService.getNatureOfContentTerms(requestQueryParameters));
    JSONObject actualNatureOfContentTerm = natureOfContentTermsResponse.getJSONArray("natureOfContentTerms")
        .getJSONObject(0);

    assertEquals(3, natureOfContentTermsResponse.get(TOTAL_RECORDS));
    assertEquals("dc25b657-d892-4b7e-adf4-67e47af18064", actualNatureOfContentTerm.get(ID));
    assertEquals("audiobook", actualNatureOfContentTerm.get(NAME));
    assertEquals("folio", actualNatureOfContentTerm.get(SOURCE));
  }

  @Test
  void getModesOfIssuance_shouldReturnModesOfIssuance() throws JSONException {
    String expectedModesOfIssuance = TestUtil.readFileContentFromResources(MODES_OF_ISSUANCE_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getModesOfIssuance(requestQueryParameters))
        .thenReturn(expectedModesOfIssuance);

    JSONObject modesOfIssuance = new JSONObject(inventoryService.getModesOfIssuance(requestQueryParameters));
    JSONObject actualNatureOfContentTerm = modesOfIssuance.getJSONArray("issuanceModes").getJSONObject(0);

    assertEquals(2, modesOfIssuance.get(TOTAL_RECORDS));
    assertEquals("9d18a02f-5897-4c31-9106-c9abb5c7ae8b", actualNatureOfContentTerm.get(ID));
    assertEquals("single unit", actualNatureOfContentTerm.get(NAME));
    assertEquals("rdamodeissue", actualNatureOfContentTerm.get(SOURCE));
  }

  @Test
  void getInstanceFormats_shouldReturnInstanceFormats() throws JSONException {
    String expectedInstanceFormatsResponse = TestUtil.readFileContentFromResources(INSTANCE_FORMATS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getInstanceFormats(requestQueryParameters))
        .thenReturn(expectedInstanceFormatsResponse);

    JSONObject instanceFormats = new JSONObject(inventoryService.getInstanceFormats(requestQueryParameters));
    JSONObject actualInstanceFormat = instanceFormats.getJSONArray("instanceFormats").getJSONObject(0);

    assertEquals(2, instanceFormats.get(TOTAL_RECORDS));
    assertEquals("88f58dc0-4243-4c6b-8321-70244ff34a83", actualInstanceFormat.get(ID));
    assertEquals("computer -- computer chip cartridge", actualInstanceFormat.get(NAME));
    assertEquals("rdacarrier", actualInstanceFormat.get(SOURCE));
  }

  @Test
  void getItems_shouldReturnItems() throws JSONException {
    String expectedInstanceFormatsResponse = TestUtil.readFileContentFromResources(ITEMS_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getItems(requestQueryParameters))
        .thenReturn(expectedInstanceFormatsResponse);

    JSONObject itemsJson = new JSONObject(inventoryService.getItems(requestQueryParameters));
    JSONObject items = itemsJson.getJSONArray("items").getJSONObject(0);

    assertEquals(1, itemsJson.get(TOTAL_RECORDS));
    assertEquals("7212ba6a-8dcf-45a1-be9a-ffaa847c4423", items.get(ID));
    assertEquals("TK 45105.88815 A58 42004 FT MEADE COPY 12", items.get(EFFECTIVE_SHELVING_ORDER));
  }

  @Test
  void getInstanceTypes_shouldReturnInstanceTypes() throws JSONException {
    String expectedInstanceFormatsResponse = TestUtil.readFileContentFromResources(INSTANCE_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getInstanceTypes(requestQueryParameters))
        .thenReturn(expectedInstanceFormatsResponse);

    JSONObject itemsJson = new JSONObject(inventoryService.getInstanceTypes(requestQueryParameters));
    JSONObject firstInstanceType = itemsJson.getJSONArray("instanceTypes").getJSONObject(0);
    JSONObject secondInstanceType = itemsJson.getJSONArray("instanceTypes").getJSONObject(1);

    assertEquals(2, itemsJson.get(TOTAL_RECORDS));
    assertEquals("a2c91e87-6bab-44d6-8adb-1fd02481fc4f", firstInstanceType.get(ID));
    assertEquals("other", firstInstanceType.get(NAME));
    assertEquals("xxx", firstInstanceType.get(CODE));
    assertEquals("rdacontent", firstInstanceType.get(SOURCE));
    assertEquals("8105bd44-e7bd-487e-a8f2-b804a361d92f", secondInstanceType.get(ID));
    assertEquals("tactile text", secondInstanceType.get(NAME));
    assertEquals("tct", secondInstanceType.get(CODE));
    assertEquals("rdacontent", secondInstanceType.get(SOURCE));
  }

  @Test
  void getInstanceNoteTypes_shouldReturnInstanceNoteTypes() throws JSONException {
    String expectedInstanceNoteTypesResponse = TestUtil.readFileContentFromResources(INSTANCE_NOTE_TYPES_RESPONSE_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getInstanceNoteTypes(requestQueryParameters))
        .thenReturn(expectedInstanceNoteTypesResponse);

    JSONObject itemsJson = new JSONObject(inventoryService.getInstanceNoteTypes(requestQueryParameters));
    JSONObject firstInstanceType = itemsJson.getJSONArray("instanceNoteTypes").getJSONObject(0);
    JSONObject secondInstanceType = itemsJson.getJSONArray("instanceNoteTypes").getJSONObject(1);

    assertEquals(2, itemsJson.get(TOTAL_RECORDS));
    assertEquals("b6f7a05c-6eb5-46cd-9d62-ca2700171190", firstInstanceType.get(ID));
    assertEquals("Note", firstInstanceType.get(NAME));
    assertEquals("folio", firstInstanceType.get(SOURCE));
    assertEquals("a81057f5-2a4c-4fb5-9ddd-9aa0e11c4ab4", secondInstanceType.get(ID));
    assertEquals("Action note", secondInstanceType.get(NAME));
    assertEquals("folio", secondInstanceType.get(SOURCE));
  }

  @Test
  void getAlternativeTitleTypes_shouldReturnAlternativeTitleTypes() throws JSONException {
    String expectedInstanceFormatsResponse = TestUtil.readFileContentFromResources(GET_ALTERNATIVE_TITLE_TYPES_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getAlternativeTitleTypes(requestQueryParameters))
        .thenReturn(expectedInstanceFormatsResponse);

    JSONObject itemsJson = new JSONObject(inventoryService.getAlternativeTitleTypes(requestQueryParameters));
    JSONObject firstAlternativeTitleTypes = itemsJson.getJSONArray("alternativeTitleTypes").getJSONObject(0);
    JSONObject secondAlternativeTitleTypes = itemsJson.getJSONArray("alternativeTitleTypes").getJSONObject(1);

    assertEquals(2, itemsJson.get(TOTAL_RECORDS));
    assertEquals("0fe58901-183e-4678-a3aa-0b4751174ba8", firstAlternativeTitleTypes.get(ID));
    assertEquals("No type specified", firstAlternativeTitleTypes.get(NAME));
    assertEquals("folio", firstAlternativeTitleTypes.get(SOURCE));
    assertEquals("30512027-cdc9-4c79-af75-1565b3bd888d", secondAlternativeTitleTypes.get(ID));
    assertEquals("Uniform title", secondAlternativeTitleTypes.get(NAME));
    assertEquals("folio", secondAlternativeTitleTypes.get(SOURCE));
  }


  @Test
  void getInventoryViewInstances_shouldReturnInventoryViewInstances() throws JSONException {
    String expectedInstanceFormatsResponse = TestUtil.readFileContentFromResources(GET_VIEW_INSTANCES_PATH);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(inventoryClient
        .getInventoryViewInstances(requestQueryParameters, false))
        .thenReturn(expectedInstanceFormatsResponse);

    JSONObject itemsJson = new JSONObject(inventoryService.getInventoryViewInstances(requestQueryParameters, false));
    JSONObject firstViewInstance = itemsJson.getJSONArray("instances").getJSONObject(0);
    JSONObject secondViewInstance = itemsJson.getJSONArray("instances").getJSONObject(1);

    assertEquals(2, itemsJson.get(TOTAL_RECORDS));
    assertEquals("f7e82a1e-fc06-4b82-bb1d-da326cb378ce", firstViewInstance.get("instanceId"));
    assertEquals(false, firstViewInstance.get("isBoundWith"));
    assertEquals("30fcc8e7-a019-43f4-b642-2edc389f4501", secondViewInstance.get("instanceId"));
    assertEquals(false, secondViewInstance.get("isBoundWith"));
  }

  @Test
  void getInstitutionById_shouldReturnInstitution() throws JSONException {
    String expectedInstitutionResponse = TestUtil.readFileContentFromResources(INSTITUTION_BY_ID_PATH);
    when(inventoryClient
        .getInstitutionById(INSTITUTION_ID))
        .thenReturn(expectedInstitutionResponse);

    JSONObject institutionJson = new JSONObject(inventoryService.getInstitutionById(INSTITUTION_ID));

    assertEquals(INSTITUTION_ID, institutionJson.get("id"));
    assertEquals("Community College", institutionJson.get("name"));
    assertEquals("CC", institutionJson.get("code"));
  }

  @Test
  void getLibraryById_shouldReturnLibrary() throws JSONException {
    String expectedLibraryResponse = TestUtil.readFileContentFromResources(LIBRARY_BY_ID_PATH);
    when(inventoryClient
        .getLibraryById(LIBRARY_ID))
        .thenReturn(expectedLibraryResponse);

    JSONObject libraryJson = new JSONObject(inventoryService.getLibraryById(LIBRARY_ID));

    assertEquals(LIBRARY_ID, libraryJson.get("id"));
    assertEquals("Datalogisk Institut", libraryJson.get("name"));
    assertEquals("DI", libraryJson.get("code"));
    assertEquals("62cf76b7-cca5-4d33-9217-edf42ce1a848", libraryJson.get("campusId"));
  }

  @Test
  void getCampusById_shouldReturnCampus() throws JSONException {
    String expectedCampusResponse = TestUtil.readFileContentFromResources(CAMPUS_BY_ID_PATH);
    when(inventoryClient
        .getCampusById(CAMPUS_ID))
        .thenReturn(expectedCampusResponse);

    JSONObject campusJson = new JSONObject(inventoryService.getCampusById(CAMPUS_ID));

    assertEquals(CAMPUS_ID, campusJson.get("id"));
    assertEquals("City Campus", campusJson.get("name"));
    assertEquals("CC", campusJson.get("code"));
    assertEquals("40ee00ca-a518-4b49-be01-0638d0a4ac57", campusJson.get("institutionId"));
  }

  @Test
  void getMaterialTypeById_shouldReturnMaterialType() throws JSONException {
    String expectedMaterialTypeResponse = TestUtil.readFileContentFromResources(MATERIAL_TYPE_BY_ID_PATH);
    when(inventoryClient
        .getMaterialTypeById(MATERIAL_TYPE_ID))
        .thenReturn(expectedMaterialTypeResponse);

    JSONObject materialTypeJson = new JSONObject(inventoryService.getMaterialTypeById(MATERIAL_TYPE_ID));

    assertEquals(MATERIAL_TYPE_ID, materialTypeJson.get("id"));
    assertEquals("Audio CD", materialTypeJson.get("name"));
    assertEquals("local", materialTypeJson.get("source"));
  }

  @Test
  void getSourceRecordById_shouldReturnSourceRecord() throws JSONException {
    String expectedSourceRecord = TestUtil.readFileContentFromResources(SOURCE_RECORD_RESPONSE_PATH);
    when(inventoryClient
        .getSourceRecords(VALID_INSTANCE_ID))
        .thenReturn(expectedSourceRecord);

    JSONObject expected = new JSONObject(expectedSourceRecord);
    JSONObject actual = new JSONObject(inventoryClient.getSourceRecords(VALID_INSTANCE_ID));

    assertEquals(VALID_INSTANCE_ID, actual.get("id"));
    assertEquals("11a796d5-923d-42a6-b9ad-733a4a10b2d9", actual.get("snapshotId"));
    assertEquals(VALID_INSTANCE_ID, actual.getJSONObject("externalIdsHolder").get("instanceId"));
    assertEquals(VALID_INSTANCE_ID, actual.getJSONObject("rawRecord").get("id"));
    assertEquals(expected.getJSONObject("rawRecord").get("content"), actual.getJSONObject("rawRecord").get("content"));
    assertEquals(41, actual.getJSONObject("parsedRecord").getJSONObject("content").getJSONArray("fields").length());
    assertEquals("02078nam a2200517Li 4500",
        actual.getJSONObject("parsedRecord").getJSONObject("content").get("leader"));
  }

  @Test
  void getAuthoritySourceRecordById_shouldReturnAuthoritySourceRecord() throws JSONException {
    String expectedSourceRecord = TestUtil.readFileContentFromResources(AUTHORITY_SOURCE_RECORD_RESPONSE_PATH);
    when(inventoryClient
            .getAuthoritySourceRecords(VALID_AUTHORITY_ID))
            .thenReturn(expectedSourceRecord);

    JSONObject expected = new JSONObject(expectedSourceRecord);
    JSONObject actual = new JSONObject(inventoryClient.getAuthoritySourceRecords(VALID_AUTHORITY_ID));

    assertEquals("73f57292-6c90-4a4e-8b16-21cf383a62b5", actual.get("id"));
    assertEquals("e82ffbaa-49d3-4f07-a8fa-1ea660cde325", actual.get("snapshotId"));
    assertEquals(VALID_AUTHORITY_ID, actual.getJSONObject("externalIdsHolder").get("authorityId"));
    assertEquals("73f57292-6c90-4a4e-8b16-21cf383a62b5", actual.getJSONObject("rawRecord").get("id"));
    assertEquals(expected.getJSONObject("rawRecord").get("content"), actual.getJSONObject("rawRecord").get("content"));
    assertEquals("01522cz  a2200313n  4500",
            actual.getJSONObject("parsedRecord").getJSONObject("content").get("leader"));
  }
}
