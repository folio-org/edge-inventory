package org.folio.edge.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryService {

  private final InventoryClient inventoryClient;

  public String getInstance(String instanceId, String lang) {
    return inventoryClient.getInstance(instanceId, lang);
  }

  public String getInstancesByQuery(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getInstancesByQuery(requestQueryParameters);
  }

  public String getHoldings(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getHoldings(requestQueryParameters);
  }

  public String getIdentifierTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getIdentifierTypes(requestQueryParameters);
  }

  public String getServicePoints(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getServicePoints(requestQueryParameters);
  }

  public String getLocationById(String locationId) {
    return inventoryClient.getLocationById(locationId);
  }

  public String getLocations(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getLocations(requestQueryParameters);
  }

  public String getInstitutionById(String institutionById) {
    return inventoryClient.getInstitutionById(institutionById);
  }

  public String getLibraryById(String libraryId) {
    return inventoryClient.getLibraryById(libraryId);
  }

  public String getCampusById(String campusId) {
    return inventoryClient.getCampusById(campusId);
  }

  public String getNatureOfContentTerms(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getNatureOfContentTerms(requestQueryParameters);
  }

  public String getModesOfIssuance(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getModesOfIssuance(requestQueryParameters);
  }

  public String getInstanceFormats(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getInstanceFormats(requestQueryParameters);
  }

  public String getItems(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getItems(requestQueryParameters);
  }

  public String getInstanceTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getInstanceTypes(requestQueryParameters);
  }

  public String getClassificationTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getClassificationTypes(requestQueryParameters);
  }

  public String getContributorTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getContributorTypes(requestQueryParameters);
  }

  public String getContributorNameTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getContributorNameTypes(requestQueryParameters);
  }

  public String getInstanceNoteTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getInstanceNoteTypes(requestQueryParameters);
  }

  public String getAlternativeTitleTypes(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getAlternativeTitleTypes(requestQueryParameters);
  }

  public String getInventoryViewInstances(RequestQueryParameters requestQueryParameters, Boolean withBoundedItems) {
    return inventoryClient.getInventoryViewInstances(requestQueryParameters, withBoundedItems);
  }

  public String getMaterialTypeById(String materialTypeId) {
    return inventoryClient.getMaterialTypeById(materialTypeId);
  }

  public String getSourceRecords(String id) {
    return inventoryClient.getSourceRecords(id);
  }

  public String getAuthoritySourceRecords(String id) {
    return inventoryClient.getAuthoritySourceRecords(id);
  }

  public String getAuthority(String authorityId) {
    return inventoryClient.getAuthority(authorityId);
  }

  public String getAuthoritiesByQuery(RequestQueryParameters requestQueryParameters) {
    return inventoryClient.getAuthoritiesByQuery(requestQueryParameters);
  }
}
