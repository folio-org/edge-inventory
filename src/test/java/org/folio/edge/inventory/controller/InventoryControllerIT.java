package org.folio.edge.inventory.controller;

import static org.folio.edge.inventory.TestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.inventory.BaseIntegrationTests;
import org.folio.edge.inventory.TestUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(OrderAnnotation.class)
class InventoryControllerIT extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getInstance_shouldReturnInstance_whenInstanceIdValid() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_VALID_INSTANCE_ID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is("e6bc03c6-c137-4221-b679-a7c5c31f986c")))
        .andExpect(jsonPath("hrid", is("inst000000000027")))
        .andExpect(
            jsonPath("title", is("Organisations- und Prozessentwicklung Harald Augustin (Hrsg.)")))
        .andExpect(jsonPath("source", is("FOLIO")));
  }

  @Test
  void getInstance_shouldReturn400Error_whenLangParameterIsNotValid() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_VALID_INSTANCE_ID_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errorMessage", is("getInstance.lang: must match \"[a-zA-Z]{2}\"")));
  }

  @Test
  void getInstance_shouldReturn400Error_wheInstanceIdIsInvalid() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_INVALID_INSTANCE_ID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getInstance_shouldReturn404_whenInstanceIdNotFound() throws Exception {
    doGetWithParams(mockMvc, INSTANCE_BY_NOT_FOUND_INSTANCE_ID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isNotFound());
  }

  @Test
  void getAuthority_shouldReturnAuthority_whenAuthorityIdValid() throws Exception {
    doGet(mockMvc, AUTHORITY_BY_VALID_AUTHORITY_ID_URL)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is("9eba0866-8195-457c-ac72-dbfc279cc496")))
            .andExpect(jsonPath("personalName", is("Woodson, Jacqueline C410821")))
            .andExpect(
                    jsonPath("naturalId", is("n88234700410821")))
            .andExpect(jsonPath("source", is("MARC")));
  }

  @Test
  void getAuthority_shouldReturn400Error_whenAuthorityIdIsInvalid() throws Exception {
    doGet(mockMvc, AUTHORITY_BY_INVALID_AUTHORITY_ID_URL)
            .andExpect(status().isBadRequest());
  }

  @Test
  void getAuthority_shouldReturn404_whenAuthorityIdNotFound() throws Exception {
    doGet(mockMvc, AUTHORITY_BY_NOT_FOUND_AUTHORITY_ID_URL)
            .andExpect(status().isNotFound());
  }

  @Test
  void getHoldings_shouldReturnHoldings() throws Exception {
    doGetWithParams(mockMvc, GET_HOLDINGS_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("holdingsRecords[0].id", is("67cd0046-e4f1-4e4f-9024-adf0b0039d09")))
        .andExpect(jsonPath("holdingsRecords[0].hrid", is("hold000000000007")))
        .andExpect(jsonPath("holdingsRecords[0].permanentLocationId", is("f34d27c6-a8eb-461b-acd6-5dea81771e70")))
        .andExpect(jsonPath("holdingsRecords[0].callNumber", is("D15.H63 A3 2002")))
        .andExpect(jsonPath("holdingsRecords[1].id", is("e9285a1c-1dfc-4380-868c-e74073003f43")))
        .andExpect(jsonPath("holdingsRecords[1].hrid", is("hold000000000011")))
        .andExpect(jsonPath("holdingsRecords[1].permanentLocationId", is("fcd64ce1-6995-48f0-840e-89ffa2288371")))
        .andExpect(jsonPath("holdingsRecords[1].callNumber", is("M1366.S67 T73 2017")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getHoldings_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_HOLDINGS_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getHoldings_shouldReturn200WithEmptyRecords_whenNoHoldingsFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_HOLDINGS_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getIdentifierTypes_shouldReturnIdentifierType() throws Exception {
    doGetWithParams(mockMvc, GET_EDGE_IDENTIFIER_TYPES_BY_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("identifierTypes[0].id", is("7f907515-a1bf-4513-8a38-92e1a07c539d")))
        .andExpect(jsonPath("identifierTypes[0].name", is("ASIN")))
        .andExpect(jsonPath("identifierTypes[0].source", is("folio")))
        .andExpect(jsonPath("totalRecords", is(1)));
  }

  @Test
  void getIdentifierTypes_shouldReturn400Error_whenLangParameterIsNotValid() throws Exception {
    doGetWithParams(mockMvc, GET_EDGE_IDENTIFIER_TYPES_BY_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getIdentifierTypes_shouldReturn200WithEmptyRecords_whenNoTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_EDGE_IDENTIFIER_TYPES_BY_VALID_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getLocations_shouldReturnLocations() throws Exception {
    doGetWithParams(mockMvc, GET_LOCATION_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("locations[0].id", is("b241764c-1466-4e1d-a028-1a3684a5da87")))
        .andExpect(jsonPath("locations[0].name", is("Popular Reading Collection")))
        .andExpect(jsonPath("locations[0].institutionId", is("40ee00ca-a518-4b49-be01-0638d0a4ac57")))
        .andExpect(jsonPath("locations[1].id", is("758258bc-ecc1-41b8-abca-f7b610822ffd")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getLocations_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_LOCATION_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getLocations_shouldReturn200WithEmptyRecords_whenNoLocationsFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_LOCATION_VALID_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getServicePoints_shouldReturnServicePoints() throws Exception {
    doGetWithParams(mockMvc, GET_SERVICE_POINTS_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("servicepoints[0].id", is("7c5abc9f-f3d7-4856-b8d7-6712462ca007")))
        .andExpect(jsonPath("servicepoints[0].name", is("Online")))
        .andExpect(jsonPath("servicepoints[0].code", is("Online")))
        .andExpect(jsonPath("servicepoints[1].id", is("c4c90014-c8c9-4ade-8f24-b5e313319f4b")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getServicePoints_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_SERVICE_POINTS_VALID_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getServicePoints_shouldReturn200WithEmptyRecords_whenNoServicePointsFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_LOCATION_VALID_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getNatureOfContentTerms_shouldReturnNatureOfContentTerms() throws Exception {
    doGetWithParams(mockMvc, GET_NATURE_OF_CONTENT_TERMS_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("natureOfContentTerms[0].id", is("dc25b657-d892-4b7e-adf4-67e47af18064")))
        .andExpect(jsonPath("natureOfContentTerms[0].name", is("audiobook")))
        .andExpect(jsonPath("natureOfContentTerms[0].source", is("folio")))
        .andExpect(jsonPath("natureOfContentTerms[1].id", is("5621fb72-8282-4819-9b89-61349ff88027")))
        .andExpect(jsonPath("natureOfContentTerms[1].name", is("autobiography")))
        .andExpect(jsonPath("natureOfContentTerms[1].source", is("folio")))
        .andExpect(jsonPath("natureOfContentTerms[2].id", is("f9f9e23d-c7f8-446c-97c9-d173f604ad50")))
        .andExpect(jsonPath("natureOfContentTerms[2].name", is("bibliography")))
        .andExpect(jsonPath("natureOfContentTerms[2].source", is("folio")))
        .andExpect(jsonPath("totalRecords", is(3)));
  }

  @Test
  void getNatureOfContentTerms_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_NATURE_OF_CONTENT_TERMS_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getNatureOfContentTerms_shouldReturn200WithEmptyRecords_whenNoNatureOfContentTermsFoundByQuery()
      throws Exception {
    doGetWithParams(mockMvc, GET_NATURE_OF_CONTENT_TERMS_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getModesOfIssuance_shouldReturnModesOfIssuance() throws Exception {
    doGetWithParams(mockMvc, GET_MODES_OF_ISSUANCE_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("issuanceModes[0].id", is("9d18a02f-5897-4c31-9106-c9abb5c7ae8b")))
        .andExpect(jsonPath("issuanceModes[0].name", is("single unit")))
        .andExpect(jsonPath("issuanceModes[0].source", is("rdamodeissue")))
        .andExpect(jsonPath("issuanceModes[1].id", is("4fc0f4fe-06fd-490a-a078-c4da1754e03a")))
        .andExpect(jsonPath("issuanceModes[1].name", is("integrating resource")))
        .andExpect(jsonPath("issuanceModes[1].source", is("rdamodeissue")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getModesOfIssuance_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_MODES_OF_ISSUANCE_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getModesOfIssuance_shouldReturn200WithEmptyRecords_whenNoModesOfIssuanceFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_MODES_OF_ISSUANCE_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getInstanceFormats_shouldReturnInstanceFormats() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_FORMATS_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("instanceFormats[0].id", is("88f58dc0-4243-4c6b-8321-70244ff34a83")))
        .andExpect(jsonPath("instanceFormats[0].name", is("computer -- computer chip cartridge")))
        .andExpect(jsonPath("instanceFormats[0].source", is("rdacarrier")))
        .andExpect(jsonPath("instanceFormats[0].code", is("cb")))
        .andExpect(jsonPath("instanceFormats[1].id", is("7fde4e21-00b5-4de4-a90a-08a84a601aeb")))
        .andExpect(jsonPath("instanceFormats[1].name", is("audio -- audio roll")))
        .andExpect(jsonPath("instanceFormats[1].source", is("rdacarrier")))
        .andExpect(jsonPath("instanceFormats[1].code", is("sq")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getInstanceFormats_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_FORMATS_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getInstanceFormats_shouldReturn200WithEmptyRecords_whenNoInstanceFormatsFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_FORMATS_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getItems_shouldReturnItems() throws Exception {
    doGetWithParams(mockMvc, GET_ITEMS_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("items[0].id", is("7212ba6a-8dcf-45a1-be9a-ffaa847c4423")))
        .andExpect(jsonPath("items[0].status.name", is("Available")))
        .andExpect(jsonPath("items[0].title", is("A semantic web primer")))
        .andExpect(jsonPath("items[0].callNumber", is("TK5105.88815 . A58 2004 FT MEADE")))
        .andExpect(jsonPath("items[0].hrid", is("item000000000014")))
        .andExpect(jsonPath("items[0].contributorNames[0].name", is("Antoniou, Grigoris")))
        .andExpect(jsonPath("items[0].contributorNames[1].name", is("Van Harmelen, Frank")))
        .andExpect(jsonPath("items[0].holdingsRecordId", is("e3ff6133-b9a2-4d4c-a1c9-dc1867d4df19")))
        .andExpect(jsonPath("items[0].barcode", is("10101")))
        .andExpect(jsonPath("items[0].itemLevelCallNumber", is("TK5105.88815 . A58 2004 FT MEADE")))
        .andExpect(jsonPath("items[0].copyNumber", is("Copy 2")))
        .andExpect(
            jsonPath("items[0].electronicAccess[0].uri", is("http://www.loc.gov/catdir/toc/ecip0718/2007020429.html")))
        .andExpect(jsonPath("items[0].electronicAccess[0].linkText", is("Links available")))
        .andExpect(jsonPath("items[0].electronicAccess[0].materialsSpecification", is("Table of contents")))
        .andExpect(jsonPath("items[0].electronicAccess[0].publicNote", is("Table of contents only")))
        .andExpect(jsonPath("items[0].electronicAccess[0].relationshipId", is("3b430592-2e09-4b48-9a0c-0636d66b9fb3")))
        .andExpect(jsonPath("items[0].statisticalCodeIds[0]", is("b5968c9e-cddc-4576-99e3-8e60aed8b0dd")))
        .andExpect(jsonPath("items[0].materialType.id", is("1a54b431-2e4f-452d-9cae-9cee66c9a892")))
        .andExpect(jsonPath("items[0].materialType.name", is("book")))
        .andExpect(jsonPath("items[0].permanentLoanType.id", is("2b94c631-fca9-4892-a730-03ee529ffe27")))
        .andExpect(jsonPath("items[0].permanentLoanType.name", is("Can circulate")))
        .andExpect(jsonPath("items[0].permanentLocation.id", is("fcd64ce1-6995-48f0-840e-89ffa2288371")))
        .andExpect(jsonPath("items[0].permanentLocation.name", is("Main Library")))
        .andExpect(jsonPath("items[0].links.self",
            is("http://127.0.0.1:9130/inventory/items/7212ba6a-8dcf-45a1-be9a-ffaa847c4423")))
        .andExpect(
            jsonPath("items[0].effectiveCallNumberComponents.callNumber", is("TK5105.88815 . A58 2004 FT MEADE")))
        .andExpect(
            jsonPath("items[0].effectiveCallNumberComponents.typeId", is("512173a7-bd09-490e-b773-17d83f2b63fe")))
        .andExpect(jsonPath("items[0].effectiveShelvingOrder", is("TK 45105.88815 A58 42004 FT MEADE COPY 12")))
        .andExpect(jsonPath("items[0].isBoundWith", is(false)))
        .andExpect(jsonPath("items[0].effectiveLocation.id", is("fcd64ce1-6995-48f0-840e-89ffa2288371")))
        .andExpect(jsonPath("items[0].effectiveLocation.name", is("Main Library")))
        .andExpect(jsonPath("totalRecords", is(1)));
  }

  @Test
  void getItems_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_ITEMS_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getItems_shouldReturn200WithEmptyRecords_whenNoItemsFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_ITEMS_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getInstanceTypes_shouldReturn200WithEmptyRecords_whenNoInstanceTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_TYPES_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getInstanceTypes_shouldReturnInstanceTypes() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("instanceTypes[0].id", is("a2c91e87-6bab-44d6-8adb-1fd02481fc4f")))
        .andExpect(jsonPath("instanceTypes[0].name", is("other")))
        .andExpect(jsonPath("instanceTypes[0].code", is("xxx")))
        .andExpect(jsonPath("instanceTypes[0].source", is("rdacontent")))
        .andExpect(jsonPath("instanceTypes[1].id", is("8105bd44-e7bd-487e-a8f2-b804a361d92f")))
        .andExpect(jsonPath("instanceTypes[1].name", is("tactile text")))
        .andExpect(jsonPath("instanceTypes[1].code", is("tct")))
        .andExpect(jsonPath("instanceTypes[1].source", is("rdacontent")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getClassificationTypes_shouldReturn200WithEmptyRecords_whenNoClassificationTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_CLASSIFICATION_TYPES_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getClassificationTypes_shouldReturnClassificationTypes() throws Exception {
    doGetWithParams(mockMvc, GET_CLASSIFICATION_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("classificationTypes[0].id", is("ad615f6e-e28c-4343-b4a0-457397c5be3e")))
        .andExpect(jsonPath("classificationTypes[0].name", is("Canadian Classification")))
        .andExpect(jsonPath("classificationTypes[0].source", is("consortium")))
        .andExpect(jsonPath("totalRecords", is(10)));
  }

  @Test
  void getContributorTypes_shouldReturn200WithEmptyRecords_whenNoContributorTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_CONTRIBUTOR_TYPES_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getContributorTypes_shouldReturnContributorTypes() throws Exception {
    doGetWithParams(mockMvc, GET_CONTRIBUTOR_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("contributorTypes[0].id", is("7131e7b8-84fa-48bd-a725-14050be38f9f")))
        .andExpect(jsonPath("contributorTypes[0].name", is("Actor")))
        .andExpect(jsonPath("contributorTypes[0].code", is("act")))
        .andExpect(jsonPath("contributorTypes[0].source", is("consortium")))
        .andExpect(jsonPath("totalRecords", is(268)));
  }

  @Test
  void getInstanceNoteTypes_shouldReturn200WithEmptyRecords_whenNoInstanceNoteTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_NOTE_TYPES_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getInstanceNoteTypes_shouldReturnInstanceNoteTypes() throws Exception {
    doGetWithParams(mockMvc, GET_INSTANCE_NOTE_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("instanceNoteTypes[0].id", is("b6f7a05c-6eb5-46cd-9d62-ca2700171190")))
        .andExpect(jsonPath("instanceNoteTypes[0].name", is("Note")))
        .andExpect(jsonPath("instanceNoteTypes[0].source", is("folio")))
        .andExpect(jsonPath("instanceNoteTypes[1].id", is("a81057f5-2a4c-4fb5-9ddd-9aa0e11c4ab4")))
        .andExpect(jsonPath("instanceNoteTypes[1].name", is("Action note")))
        .andExpect(jsonPath("instanceNoteTypes[1].source", is("folio")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getAlternativeTitleTypes_shouldReturnAlternativeTitleTypes() throws Exception {
    doGetWithParams(mockMvc, GET_ALTERNATIVE_TITLE_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("alternativeTitleTypes[0].id", is("0fe58901-183e-4678-a3aa-0b4751174ba8")))
        .andExpect(jsonPath("alternativeTitleTypes[0].name", is("No type specified")))
        .andExpect(jsonPath("alternativeTitleTypes[0].source", is("folio")))
        .andExpect(jsonPath("alternativeTitleTypes[1].id", is("30512027-cdc9-4c79-af75-1565b3bd888d")))
        .andExpect(jsonPath("alternativeTitleTypes[1].name", is("Uniform title")))
        .andExpect(jsonPath("alternativeTitleTypes[1].source", is("folio")))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getAlternativeTitleTypes_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_ALTERNATIVE_TITLE_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getInventoryViewInstances_shouldReturnInventoryViewInstances() throws Exception {
    doGetWithParams(mockMvc, GET_VIEW_INSTANCES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("instances[0].instanceId", is("f7e82a1e-fc06-4b82-bb1d-da326cb378ce")))
        .andExpect(jsonPath("instances[0].isBoundWith", is(false)))
        .andExpect(jsonPath("instances[1].instanceId", is("30fcc8e7-a019-43f4-b642-2edc389f4501")))
        .andExpect(jsonPath("instances[1].isBoundWith", is(false)))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getInventoryViewInstances_shouldReturn400Error_whenLangParameterIsInValid() throws Exception {
    doGetWithParams(mockMvc, GET_VIEW_INSTANCES_URL, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getInstitutionById_shouldReturnInstitution() throws Exception {
    doGet(mockMvc, GET_INSTITUTION_BY_ID_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is("6ecd8132-caef-4f87-bbb0-9fc07d71357d")))
        .andExpect(jsonPath("name", is("Community College")))
        .andExpect(jsonPath("code", is("CC")));
  }

  @Test
  void getInstitutionById_shouldReturnNotFound() throws Exception {
    doGet(mockMvc, GET_INSTITUTION_BY_ID_NOT_FOUND_URL)
        .andExpect(status().isNotFound());
  }

  @Test
  void getLibraryById_shouldReturnLibrary() throws Exception {
    doGet(mockMvc, GET_LIBRARY_BY_ID_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is("5d78803e-ca04-4b4a-aeae-2c63b924518b")))
        .andExpect(jsonPath("name", is("Datalogisk Institut")))
        .andExpect(jsonPath("code", is("DI")))
        .andExpect(jsonPath("campusId", is("62cf76b7-cca5-4d33-9217-edf42ce1a848")));

  }

  @Test
  void getLibraryById_shouldReturnNotFound() throws Exception {
    doGet(mockMvc, GET_LIBRARY_BY_ID_NOT_FOUND_URL)
        .andExpect(status().isNotFound());

  }

  @Test
  void getCampusById_shouldReturnCampus() throws Exception {
    doGet(mockMvc, GET_CAMPUS_BY_ID_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is("62cf76b7-cca5-4d33-9217-edf42ce1a848")))
        .andExpect(jsonPath("name", is("City Campus")))
        .andExpect(jsonPath("code", is("CC")))
        .andExpect(jsonPath("institutionId", is("40ee00ca-a518-4b49-be01-0638d0a4ac57")));
  }

  @Test
  void getCampusById_shouldReturnNotFound() throws Exception {
    doGet(mockMvc, GET_CAMPUS_BY_ID_NOT_FOUND_URL)
        .andExpect(status().isNotFound());
  }

  @Test
  void getMaterialTypeById_shouldReturnCampus() throws Exception {
    doGet(mockMvc, GET_MATERIAL_TYPE_BY_ID_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is("79a28446-25ed-4be6-8821-20b57cae0677")))
        .andExpect(jsonPath("name", is("Audio CD")))
        .andExpect(jsonPath("source", is("local")));
  }

  @Test
  void getMaterialTypeById_shouldReturnNotFound() throws Exception {
    doGet(mockMvc, GET_MATERIAL_TYPE_BY_ID_NOT_FOUND_URL)
        .andExpect(status().isNotFound());
  }

  @Test
  void getInstancesByQuery_shouldReturnInstances() throws Exception {
    doGet(mockMvc, INSTANCES_WITH_QUERY_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("instances[0].id", is("5b1eb450-ff9f-412d-a9e7-887f6eaeb5b4")));
  }

  @Test
  void getInstancesByQuery_shouldReturnEmpty_whenInstancesResponseIsEmpty() throws Exception {
    doGet(mockMvc, INSTANCES_WITH_QUERY_URL_NOT_FOUND)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)))
        .andExpect(jsonPath("instances", iterableWithSize(0)));
  }

  @Test
  void getSourceRecord_shouldReturnSourceRecord_whenInstanceIdValid() throws Exception {
    String expectedString = TestUtil.readFileContentFromResources(SOURCE_RECORD_RESPONSE_PATH);
    JSONObject expectedResponse = new JSONObject(expectedString);

    doGet(mockMvc, SOURCE_RECORDS_WITH_VALID_ID_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(expectedResponse.get("id"))))
        .andExpect(jsonPath("snapshotId", is(expectedResponse.get("snapshotId"))))
        .andExpect(jsonPath("rawRecord.id", is(expectedResponse.getJSONObject("rawRecord").get("id"))))
        .andExpect(jsonPath("rawRecord.content", is(expectedResponse.getJSONObject("rawRecord").get("content"))))
        .andExpect(jsonPath("parsedRecord.formattedContent", is(expectedResponse.getJSONObject("parsedRecord").get("formattedContent"))))
        .andExpect(jsonPath("parsedRecord.content.leader",
            is(expectedResponse.getJSONObject("parsedRecord").getJSONObject("content").get("leader"))))
        .andExpect(jsonPath("parsedRecord.content.fields", hasSize(
            expectedResponse.getJSONObject("parsedRecord").getJSONObject("content").getJSONArray("fields").length())))
        .andExpect(jsonPath("externalIdsHolder.instanceId",
            is(expectedResponse.getJSONObject("externalIdsHolder").get("instanceId"))));
  }

  @Test
  void getSourceRecord_shouldReturn400Error_wheInstanceIdIsInvalid() throws Exception {
    doGet(mockMvc, SOURCE_RECORDS_WITH_INVALID_ID_URL)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getSourceRecord_shouldReturn404_whenInstanceIdNotFound() throws Exception {
    doGet(mockMvc, SOURCE_RECORDS_WITH_NON_EXISTING_ID_URL)
        .andExpect(status().isNotFound());
  }

  @Test
  void getAuthoritySourceRecord_shouldReturnSourceRecord_whenAuthorityIdValid() throws Exception {
    String expectedString = TestUtil.readFileContentFromResources(AUTHORITY_SOURCE_RECORD_RESPONSE_PATH);
    JSONObject expectedResponse = new JSONObject(expectedString);

    doGet(mockMvc, AUTHORITY_SOURCE_RECORDS_WITH_VALID_ID_URL)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(expectedResponse.get("id"))))
            .andExpect(jsonPath("snapshotId", is(expectedResponse.get("snapshotId"))))
            .andExpect(jsonPath("rawRecord.id", is(expectedResponse.getJSONObject("rawRecord").get("id"))))
            .andExpect(jsonPath("rawRecord.content", is(expectedResponse.getJSONObject("rawRecord").get("content"))))
            .andExpect(jsonPath("parsedRecord.formattedContent", is(expectedResponse.getJSONObject("parsedRecord").get("formattedContent"))))
            .andExpect(jsonPath("parsedRecord.content.leader",
                    is(expectedResponse.getJSONObject("parsedRecord").getJSONObject("content").get("leader"))))
            .andExpect(jsonPath("parsedRecord.content.fields", hasSize(
                    expectedResponse.getJSONObject("parsedRecord").getJSONObject("content").getJSONArray("fields").length())))
            .andExpect(jsonPath("externalIdsHolder.authorityId",
                    is(expectedResponse.getJSONObject("externalIdsHolder").get("authorityId"))));
  }

  @Test
  void getAuthoritySourceRecord_shouldReturn400Error_whenAuthorityIdIsInvalid() throws Exception {
    doGet(mockMvc, AUTHORITY_SOURCE_RECORDS_WITH_INVALID_ID_URL)
            .andExpect(status().isBadRequest());
  }

  @Test
  void getAuthoritySourceRecord_shouldReturn404_whenInstanceIdNotFound() throws Exception {
    doGet(mockMvc, AUTHORITY_SOURCE_RECORDS_WITH_NON_EXISTING_ID_URL)
            .andExpect(status().isNotFound());
  }

  @Test
  void getContributorNameTypes_shouldReturn200WithEmptyRecords_whenNoContributorNameTypesFoundByQuery() throws Exception {
    doGetWithParams(mockMvc, GET_CONTRIBUTOR_NAME_TYPES_URL, QUERY_PARAM_NAME, "id==test")
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getContributorNameTypes_shouldReturnContributorNameTypes() throws Exception {
    doGetWithParams(mockMvc, GET_CONTRIBUTOR_NAME_TYPES_URL, LANG_PARAM_NAME, LANG_PARAM_VALID_VALUE)
        .andExpect(status().isOk())
        .andExpect(jsonPath("contributorNameTypes[0].id", is("2e48e713-17f3-4c13-a9f8-23845bb210aa")))
        .andExpect(jsonPath("contributorNameTypes[0].name", is("Corporate name")))
        .andExpect(jsonPath("contributorNameTypes[0].ordering", is("2")))
        .andExpect(jsonPath("contributorNameTypes[1].name", is("Meeting name")))
        .andExpect(jsonPath("contributorNameTypes[2].name", is("Personal name")))
        .andExpect(jsonPath("totalRecords", is(3)));
  }

  @Test
  void downloadAuthority_shouldReturnCorrectFileNameAndContentInUft_whenUtfNotProvided() throws Exception {
    doGet(mockMvc, "/inventory/download-authority/0831576a-c421-47dd-9285-ebab3351faa8")
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/octet-stream"))
        .andExpect(header().string("Content-Disposition",
            "attachment; filename=\"" + "0831576a-c421-47dd-9285-ebab3351faa8-utf.mrc" + "\""))
        .andExpect(result -> {
          byte[] expectedContent = TestUtil.readFileBytesFromResources("__files/responses/0831576a-c421-47dd-9285-ebab3351faa8-utf.mrc");
          byte[] actualContent = result.getResponse().getContentAsByteArray();
          Assertions.assertArrayEquals(expectedContent, actualContent);
        });
  }

  @Test
  void downloadAuthority_shouldReturnCorrectFileNameAndContentInMarc8_whenNonUtf() throws Exception {
    doGet(mockMvc, "/inventory/download-authority/0831576a-c421-47dd-9285-ebab3351faa8?utf=false")
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/octet-stream"))
        .andExpect(header().string("Content-Disposition",
            "attachment; filename=\"" + "0831576a-c421-47dd-9285-ebab3351faa8-marc8.mrc" + "\""))
        .andExpect(result -> {
          byte[] expectedContent = TestUtil.readFileBytesFromResources("__files/responses/0831576a-c421-47dd-9285-ebab3351faa8-marc8.mrc");
          byte[] actualContent = result.getResponse().getContentAsByteArray();
          Assertions.assertArrayEquals(expectedContent, actualContent);
        });
  }

  @Test
  void downloadAuthority_shouldReturnError_whenInventoryRespondWithError() throws Exception {
    doGet(mockMvc, "/inventory/download-authority/1831576a-c421-47dd-9285-ebab3351faa8?utf=true")
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(500))
        .andExpect(jsonPath("$.errorMessage").value("Couldn't find authority in db for ID: 1831576a-c421-47dd-9285-ebab3351faa8"));
  }

  @Test
  void downloadInstance_shouldReturnCorrectFileNameAndContentInUft_whenUtfNotProvided() throws Exception {
    doGet(mockMvc, "/inventory/download-instance/0684a01d-a83a-4ea7-8985-37dd59a751b2")
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/octet-stream"))
        .andExpect(header().string("Content-Disposition",
            "attachment; filename=\"" + "0684a01d-a83a-4ea7-8985-37dd59a751b2-utf.mrc" + "\""))
        .andExpect(result -> {
          byte[] expectedContent = TestUtil.readFileBytesFromResources("__files/responses/0684a01d-a83a-4ea7-8985-37dd59a751b2-utf.mrc");
          byte[] actualContent = result.getResponse().getContentAsByteArray();
          Assertions.assertArrayEquals(expectedContent, actualContent);
        });
  }

  @Test
  void downloadInstance_shouldReturnCorrectFileNameAndContentInMarc8_whenNonUtf() throws Exception {
    doGet(mockMvc, "/inventory/download-instance/0684a01d-a83a-4ea7-8985-37dd59a751b2?utf=false")
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/octet-stream"))
        .andExpect(header().string("Content-Disposition",
            "attachment; filename=\"" + "0684a01d-a83a-4ea7-8985-37dd59a751b2-marc8.mrc" + "\""))
        .andExpect(result -> {
          byte[] expectedContent = TestUtil.readFileBytesFromResources("__files/responses/0684a01d-a83a-4ea7-8985-37dd59a751b2-marc8.mrc");
          byte[] actualContent = result.getResponse().getContentAsByteArray();
          Assertions.assertArrayEquals(expectedContent, actualContent);
        });
  }

  @Test
  void downloadInstance_shouldReturnError_whenInventoryRespondWithError() throws Exception {
    doGet(mockMvc, "/inventory/download-instance/1831576a-c421-47dd-9285-ebab3351faa8?utf=true")
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(500))
        .andExpect(jsonPath("$.errorMessage").value("Couldn't find instance in db for ID: 1831576a-c421-47dd-9285-ebab3351faa8"));
  }
}
