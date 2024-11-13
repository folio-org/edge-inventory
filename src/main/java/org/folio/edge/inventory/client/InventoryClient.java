package org.folio.edge.inventory.client;

import org.folio.edge.inventory.config.InventoryClientConfig;
import org.folio.spring.integration.XOkapiHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory", configuration = InventoryClientConfig.class)
public interface InventoryClient {

  @GetMapping(value = "/inventory/instances/{instanceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstance(@PathVariable("instanceId") String instanceId, @RequestParam String lang);

  @GetMapping(value = "/inventory/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstancesByQuery(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/holdings-storage/holdings", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getHoldings(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/identifier-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getIdentifierTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/locations", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getLocations(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/locations/{locationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getLocationById(@PathVariable("locationId") String locationId);

  @GetMapping(value = "/location-units/institutions/{institutionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstitutionById(@PathVariable("institutionId") String institutionId);

  @GetMapping(value = "/location-units/libraries/{libraryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getLibraryById(@PathVariable("libraryId") String libraryId);

  @GetMapping(value = "/location-units/campuses/{campusId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getCampusById(@PathVariable("campusId") String campusId);

  @GetMapping(value = "/service-points", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getServicePoints(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/nature-of-content-terms", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getNatureOfContentTerms(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/modes-of-issuance", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getModesOfIssuance(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/instance-formats", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstanceFormats(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/inventory/items", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getItems(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/instance-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstanceTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/classification-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getClassificationTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/contributor-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getContributorTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/contributor-name-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getContributorNameTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/instance-note-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInstanceNoteTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/inventory-view/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInventoryViewInstances(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/inventory-view/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getInventoryViewInstances(@SpringQueryMap Object requestQueryParameters, @RequestHeader(XOkapiHeaders.TENANT) String tenantId);

  @GetMapping(value = "/alternative-title-types", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getAlternativeTitleTypes(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/material-types/{materialTypeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getMaterialTypeById(@PathVariable("materialTypeId") String materialTypeId);

  @GetMapping(value = "/source-storage/records/{instanceId}/formatted?idType=INSTANCE", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getSourceRecords(@PathVariable("instanceId") String instanceId);

  @GetMapping(value = "/source-storage/records/{authorityId}/formatted?idType=AUTHORITY", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getAuthoritySourceRecords(@PathVariable("authorityId") String authorityId);

  @GetMapping(value = "/authority-storage/authorities/{authorityId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getAuthority(@PathVariable("authorityId") String authorityId);

  @GetMapping(value = "/authority-storage/authorities", consumes = MediaType.APPLICATION_JSON_VALUE)
  String getAuthoritiesByQuery(@SpringQueryMap Object requestQueryParameters);
}
