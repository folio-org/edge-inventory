package org.folio.edge.inventory.client;

import java.util.Map;
import org.folio.spring.integration.XOkapiHeaders;
import tools.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface InventoryClient {

  @GetExchange("inventory/instances/{instanceId}")
  JsonNode getInstance(@PathVariable("instanceId") String instanceId, @RequestParam String lang);

  @GetExchange("instance-storage/instances/{instanceId}/summary")
  JsonNode getInstanceSummary(@PathVariable("instanceId") String instanceId);

  @GetExchange("inventory/instances")
  JsonNode getInstancesByQuery(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("holdings-storage/holdings")
  JsonNode getHoldings(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("holdings-storage/holdings")
  JsonNode getHoldings(@RequestParam Map<String, ?> requestQueryParameters,
      @RequestHeader(XOkapiHeaders.TENANT) String tenantId);

  @GetExchange("identifier-types")
  JsonNode getIdentifierTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("locations")
  JsonNode getLocations(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("locations/{locationId}")
  JsonNode getLocationById(@PathVariable("locationId") String locationId);

  @GetExchange("location-units/institutions/{institutionId}")
  JsonNode getInstitutionById(@PathVariable("institutionId") String institutionId);

  @GetExchange("location-units/libraries/{libraryId}")
  JsonNode getLibraryById(@PathVariable("libraryId") String libraryId);

  @GetExchange("location-units/campuses/{campusId}")
  JsonNode getCampusById(@PathVariable("campusId") String campusId);

  @GetExchange("service-points")
  JsonNode getServicePoints(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("nature-of-content-terms")
  JsonNode getNatureOfContentTerms(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("modes-of-issuance")
  JsonNode getModesOfIssuance(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("instance-formats")
  JsonNode getInstanceFormats(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("inventory/items")
  JsonNode getItems(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("inventory/items")
  JsonNode getItems(@RequestParam Map<String, ?> requestQueryParameters,
      @RequestHeader(XOkapiHeaders.TENANT) String tenantId);

  @GetExchange("instance-types")
  JsonNode getInstanceTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("classification-types")
  JsonNode getClassificationTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("contributor-types")
  JsonNode getContributorTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("contributor-name-types")
  JsonNode getContributorNameTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("instance-note-types")
  JsonNode getInstanceNoteTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("inventory-view/instances")
  JsonNode getInventoryViewInstances(@RequestParam Map<String, ?> requestQueryParameters,
      @RequestParam Boolean withBoundedItems);

  @GetExchange("inventory-view/instances")
  JsonNode getInventoryViewInstances(@RequestParam Map<String, ?> requestQueryParameters,
      @RequestParam Boolean withBoundedItems, @RequestHeader(XOkapiHeaders.TENANT) String tenantId);

  @GetExchange("alternative-title-types")
  JsonNode getAlternativeTitleTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("material-types")
  JsonNode getMaterialTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("material-types/{materialTypeId}")
  JsonNode getMaterialTypeById(@PathVariable("materialTypeId") String materialTypeId);

  @GetExchange("material-types/{materialTypeId}")
  JsonNode getMaterialTypeById(@PathVariable("materialTypeId") String materialTypeId,
      @RequestHeader(XOkapiHeaders.TENANT) String tenantId);

  @GetExchange("source-storage/records/{instanceId}/formatted?idType=INSTANCE")
  JsonNode getSourceRecords(@PathVariable("instanceId") String instanceId);

  @GetExchange("source-storage/records/{authorityId}/formatted?idType=AUTHORITY")
  JsonNode getAuthoritySourceRecords(@PathVariable("authorityId") String authorityId);

  @GetExchange("authority-storage/authorities/{authorityId}")
  JsonNode getAuthority(@PathVariable("authorityId") String authorityId);

  @GetExchange("authority-storage/authorities")
  JsonNode getAuthoritiesByQuery(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("subject-sources")
  JsonNode getSubjectSources(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("subject-types")
  JsonNode getSubjectTypes(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("holdings-note-types")
  JsonNode getHoldingsNoteTypes(@RequestParam Map<String, ?> requestQueryParameters);
}
