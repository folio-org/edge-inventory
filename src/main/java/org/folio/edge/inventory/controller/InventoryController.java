package org.folio.edge.inventory.controller;

import org.folio.edge.inventory.service.DataExportService;
import org.folio.edge.inventory.service.InventoryService;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.folio.inventory.rest.resource.InventoryApi;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Edge inventory")
@Log4j2
@RestController
@RequiredArgsConstructor
public class InventoryController implements InventoryApi {

  private final InventoryService inventoryService;
  private final DataExportService dataExportService;

  @Override
  public ResponseEntity<String> getInstance(String instanceId, String xOkapiTenant, String xOkapiToken, String lang) {
    log.info("Retrieving instance by given id {}", instanceId);
    return ResponseEntity.ok(inventoryService.getInstance(instanceId, lang));
  }

  @Override
  public ResponseEntity<String> getInstancesByQuery(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving instances by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getInstancesByQuery(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getHoldings(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving holdings  by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getHoldings(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getInstanceFormats(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving instance formats by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getInstanceFormats(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getLocations(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving locations by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getLocations(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getInstitutionById(String institutionId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving institution by id {}", institutionId);
    return ResponseEntity.ok(inventoryService.getInstitutionById(institutionId));
  }

  @Override
  public ResponseEntity<String> getLibraryById(String libraryId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving library by id {}", libraryId);
    return ResponseEntity.ok(inventoryService.getLibraryById(libraryId));
  }

  @Override
  public ResponseEntity<String> getCampusById(String campusId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving library by id {}", campusId);
    return ResponseEntity.ok(inventoryService.getCampusById(campusId));
  }

  @Override
  public ResponseEntity<String> getModesOfIssuance(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving modes of issuance by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getModesOfIssuance(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getServicePoints(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving service points by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getServicePoints(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getIdentifierTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving identifier types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getIdentifierTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getNatureOfContentTerms(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving nature of content terms by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getNatureOfContentTerms(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getItems(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving items by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getItems(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getInstanceTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving instance types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getInstanceTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getClassificationTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving classification types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getClassificationTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getInstanceNoteTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving instance note types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getInstanceNoteTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getAlternativeTitleTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving alternative title types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getAlternativeTitleTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getInventoryViewInstances(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving inventory view instances by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getInventoryViewInstances(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getMaterialTypeById(String materialTypeId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving material type by id {}", materialTypeId);
    return ResponseEntity.ok(inventoryService.getMaterialTypeById(materialTypeId));
  }

  @Override
  public ResponseEntity<String> getSourceRecords(String instanceId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving source records by instance id {}", instanceId);
    return ResponseEntity.ok(inventoryService.getSourceRecords(instanceId));
  }

  @Override
  public ResponseEntity<String> getAuthoritySourceRecords(String authorityId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving source records by authority id {}", authorityId);
    return ResponseEntity.ok(inventoryService.getAuthoritySourceRecords(authorityId));
  }

  @Override
  public ResponseEntity<String> getContributorTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving contributor types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getContributorTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getContributorNameTypes(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving contributor name types by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getContributorNameTypes(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getAuthoritiesByQuery(String xOkapiTenant, String xOkapiToken, RequestQueryParameters requestQueryParameters) {
    log.info("Retrieving authorities by query {}", requestQueryParameters.getQuery());
    return ResponseEntity.ok(inventoryService.getAuthoritiesByQuery(requestQueryParameters));
  }

  @Override
  public ResponseEntity<String> getAuthority(String authorityId, String xOkapiTenant, String xOkapiToken) {
    log.info("Retrieving authority by id {}", authorityId);
    return ResponseEntity.ok(inventoryService.getAuthority(authorityId));
  }

  @Override
  public ResponseEntity<Resource> downloadAuthorityById(String authorityId, String xOkapiTenant, String xOkapiToken,
      Boolean isUtf) {
    log.info("Download authority by id {} in utf format:{}", authorityId, isUtf);
    return dataExportService.downloadAuthority(authorityId, isUtf);
  }

  @Override
  public ResponseEntity<Resource> downloadInstanceById(String instanceId, String xOkapiTenant, String xOkapiToken,
      Boolean isUtf) {
    log.info("Download instance by id {} in utf format:{}", instanceId, isUtf);
    return dataExportService.downloadInstance(instanceId, isUtf);
  }
}
