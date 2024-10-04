package org.folio.edge.inventory;

import org.apache.commons.lang3.StringUtils;

public class TestConstants {

  public static final String OKAPI_URL = "http://localhost:9130";
  public static final String OKAPI_URL_FIELD = "okapiUrl";
  public static final String GET_EDGE_IDENTIFIER_TYPES_BY_VALID_URL = StringUtils
      .join(OKAPI_URL, "/inventory/identifier-types");
  public static final String INSTANCE_RESPONSE_PATH = "__files/responses/instance_response.json";
  public static final String AUTHORITY_RESPONSE_PATH = "__files/responses/authority_response.json";
  public static final String IDENTIFIER_TYPES_RESPONSE_PATH = "__files/responses/identifier_types_response.json";
  public static final String LOCATIONS_RESPONSE_PATH = "__files/responses/locations_response.json";
  public static final String SERVICE_POINTS_RESPONSE_PATH = "__files/responses/service_points_response.json";
  public static final String NATURE_OF_CONTENT_TERMS_RESPONSE_PATH = "__files/responses/nature_of_content_terms_response.json";
  public static final String MODES_OF_ISSUANCE_RESPONSE_PATH = "__files/responses/modes_of_issuance_response.json";
  public static final String INSTANCE_FORMATS_RESPONSE_PATH = "__files/responses/instance_formats_response.json";
  public static final String HOLDINGS_RESPONSE_PATH = "__files/responses/holdings_response.json";
  public static final String ITEMS_RESPONSE_PATH = "__files/responses/items_response.json";
  public static final String INSTANCE_TYPES_RESPONSE_PATH = "__files/responses/instance_types_response.json";
  public static final String CLASSIFICATION_TYPES_RESPONSE_PATH = "__files/responses/classification_types_response.json";
  public static final String CONTRIBUTOR_TYPES_RESPONSE_PATH = "__files/responses/contributor_types_response.json";
  public static final String CONTRIBUTOR_NAME_TYPES_RESPONSE_PATH = "__files/responses/contributor_name_types_response.json";
  public static final String INSTANCE_NOTE_TYPES_RESPONSE_PATH = "__files/responses/instance_note_types_response.json";
  public static final String GET_VIEW_INSTANCES_PATH = "__files/responses/view_instances_response.json";
  public static final String GET_ALTERNATIVE_TITLE_TYPES_PATH ="__files/responses/alternative_title_types_response.json";
  public static final String INSTITUTION_BY_ID_PATH ="__files/responses/institution_by_id_response.json";
  public static final String CAMPUS_BY_ID_PATH ="__files/responses/campus_by_id_response.json";
  public static final String LIBRARY_BY_ID_PATH ="__files/responses/library_by_id_response.json";
  public static final String MATERIAL_TYPE_BY_ID_PATH ="__files/responses/material_type_by_id_response.json";
  public static final String SOURCE_RECORD_RESPONSE_PATH ="__files/responses/source-records.json";
  public static final String AUTHORITY_SOURCE_RECORD_RESPONSE_PATH = "__files/responses/authority_source_records.json";
  public static final String INSTANCE_BY_ID_FORBIDDEN_URI = "/inventory/instances/7ca21665-1b61-44e4-a49a-549a7947d387";
  public static final String INSTANCE_BY_ID_NOT_FOUND_URI = "/inventory/instances/36055839-c8b0-44af-ba5e-1b7dd2b7d296";
  public static final String INSTANCE_BY_ID_NOT_AUTHORIZED_URI = "/inventory/instances/3bad75d2-763e-4959-876d-4b29fea72b02";
  public static final String LANG_PARAM_NAME = "lang";
  public static final String QUERY_PARAM_NAME = "query";
  public static final String LANG_PARAM_VALID_VALUE = "en";
  public static final String LANG_PARAM_INVALID_VALUE = "invalid";
  public static final String VALID_INSTANCE_ID = "e6bc03c6-c137-4221-b679-a7c5c31f986c";
  public static final String INSTANCE_BY_VALID_INSTANCE_ID_URL = "/inventory/instances/e6bc03c6-c137-4221-b679-a7c5c31f986c";
  public static final String INSTANCE_BY_INVALID_INSTANCE_ID_URL = "/inventory/instances/invalidid";
  public static final String INSTANCE_BY_NOT_FOUND_INSTANCE_ID_URL = "/inventory/instances/b48afa82-779c-4460-86bd-de78b6ab866f";
  public static final String VALID_AUTHORITY_ID = "9eba0866-8195-457c-ac72-dbfc279cc496";
  public static final String AUTHORITY_BY_VALID_AUTHORITY_ID_URL = "/inventory/authorities/9eba0866-8195-457c-ac72-dbfc279cc496";
  public static final String AUTHORITY_BY_INVALID_AUTHORITY_ID_URL = "/inventory/authorities/invalidid";
  public static final String AUTHORITY_BY_NOT_FOUND_AUTHORITY_ID_URL = "/inventory/authorities/b48afa82-779c-4460-86bd-de78b6ab866f";
  public static final String TEST_TENANT = "test";
  public static final String GET_LOCATION_VALID_URL = "/inventory/locations";
  public static final String GET_SERVICE_POINTS_VALID_URL = "/inventory/service-points";
  public static final String GET_NATURE_OF_CONTENT_TERMS_URL = "/inventory/nature-of-content-terms";
  public static final String GET_MODES_OF_ISSUANCE_URL = "/inventory/modes-of-issuance";
  public static final String GET_INSTANCE_FORMATS_URL = "/inventory/instance-formats";
  public static final String GET_HOLDINGS_URL = "/inventory/holdings";
  public static final String GET_ITEMS_URL = "/inventory/items";
  public static final String GET_INSTANCE_TYPES_URL = "/inventory/instance-types";
  public static final String GET_CLASSIFICATION_TYPES_URL = "/inventory/classification-types";
  public static final String GET_CONTRIBUTOR_TYPES_URL = "/inventory/contributor-types";
  public static final String GET_CONTRIBUTOR_NAME_TYPES_URL = "/inventory/contributor-name-types";
  public static final String GET_INSTANCE_NOTE_TYPES_URL = "/inventory/instance-note-types";
  public static final String GET_VIEW_INSTANCES_URL = "/inventory/inventory-view/instances";
  public static final String GET_ALTERNATIVE_TITLE_TYPES_URL = "/inventory/alternative-title-types";
  public static final String INSTITUTION_ID = "6ecd8132-caef-4f87-bbb0-9fc07d71357d";
  public static final String LIBRARY_ID = "5d78803e-ca04-4b4a-aeae-2c63b924518b";
  public static final String CAMPUS_ID = "62cf76b7-cca5-4d33-9217-edf42ce1a848";
  public static final String MATERIAL_TYPE_ID = "79a28446-25ed-4be6-8821-20b57cae0677";
  public static final String GET_INSTITUTION_BY_ID_URL = "/inventory/location-units/institutions/6ecd8132-caef-4f87-bbb0-9fc07d71357d";
  public static final String GET_INSTITUTION_BY_ID_NOT_FOUND_URL = "/inventory/location-units/institutions/6ecd8132-caef-4f87-bbb0-9fc07d713571";
  public static final String GET_CAMPUS_BY_ID_URL = "/inventory/location-units/campuses/62cf76b7-cca5-4d33-9217-edf42ce1a848";
  public static final String GET_CAMPUS_BY_ID_NOT_FOUND_URL = "/inventory/location-units/campuses/b518271c-973e-4ccf-92e7-5acfa54fd6cd";
  public static final String GET_LIBRARY_BY_ID_URL = "/inventory/location-units/libraries/5d78803e-ca04-4b4a-aeae-2c63b924518b";
  public static final String GET_LIBRARY_BY_ID_NOT_FOUND_URL = "/inventory/location-units/libraries/188a04eb-33c8-406c-bb09-711a91e18cc8";
  public static final String GET_MATERIAL_TYPE_BY_ID_URL = "/inventory/material-types/79a28446-25ed-4be6-8821-20b57cae0677";
  public static final String GET_MATERIAL_TYPE_BY_ID_NOT_FOUND_URL = "/inventory/material-types/80a28446-25ed-4be6-8821-20b57cae0688";
  public static final String INSTANCES_WITH_QUERY_URL = "/inventory/instances?query=id==5b1eb450-ff9f-412d-a9e7-887f6eaeb5b4";
  public static final String INSTANCES_WITH_QUERY_URL_NOT_FOUND = "/inventory/instances?query=id==5b1eb450-ff9f-412d-a9e7-887f6eaeb5b0";
  public static final String SOURCE_RECORDS_WITH_VALID_ID_URL = "/inventory/source-records/e6bc03c6-c137-4221-b679-a7c5c31f986c";
  public static final String SOURCE_RECORDS_WITH_NON_EXISTING_ID_URL = "/inventory/source-records/e857a059-e0b6-4d8f-8783-79f773a7752f";
  public static final String SOURCE_RECORDS_WITH_INVALID_ID_URL = "/inventory/source-records/invalid";
  public static final String AUTHORITY_SOURCE_RECORDS_WITH_VALID_ID_URL = "/inventory/authorities/source-records/9eba0866-8195-457c-ac72-dbfc279cc496";
  public static final String AUTHORITY_SOURCE_RECORDS_WITH_NON_EXISTING_ID_URL = "/inventory/authorities/source-records/e857a059-e0b6-4d8f-8783-79f773a7752f";
  public static final String AUTHORITY_SOURCE_RECORDS_WITH_INVALID_ID_URL = "/inventory/authorities/source-records/invalid";
}