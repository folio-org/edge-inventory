package org.folio.edge.inventory.service;

import lombok.RequiredArgsConstructor;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.service.mapper.RequestQueryParametersMapper;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryClient inventoryClient;
  private final RequestQueryParametersMapper requestQueryParametersMapper;

  public String getInstance(String instanceId, String lang) {
    return inventoryClient.getInstance(instanceId, lang).toString();
  }

  public String getInstanceSummary(String instanceId) {
    return inventoryClient.getInstanceSummary(instanceId).toString();
  }

  public String getInstancesByQuery(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getInstancesByQuery(requestQueryParametersMap).toString();
  }

  public String getHoldings(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getHoldings(requestQueryParametersMap).toString();
  }

  public String getIdentifierTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getIdentifierTypes(requestQueryParametersMap).toString();
  }

  public String getServicePoints(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getServicePoints(requestQueryParametersMap).toString();
  }

  public String getLocationById(String locationId) {
    return inventoryClient.getLocationById(locationId).toString();
  }

  public String getLocations(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getLocations(requestQueryParametersMap).toString();
  }

  public String getInstitutionById(String institutionById) {
    return inventoryClient.getInstitutionById(institutionById).toString();
  }

  public String getLibraryById(String libraryId) {
    return inventoryClient.getLibraryById(libraryId).toString();
  }

  public String getCampusById(String campusId) {
    return inventoryClient.getCampusById(campusId).toString();
  }

  public String getNatureOfContentTerms(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getNatureOfContentTerms(requestQueryParametersMap).toString();
  }

  public String getModesOfIssuance(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getModesOfIssuance(requestQueryParametersMap).toString();
  }

  public String getInstanceFormats(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getInstanceFormats(requestQueryParametersMap).toString();
  }

  public String getItems(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getItems(requestQueryParametersMap).toString();
  }

  public String getInstanceTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getInstanceTypes(requestQueryParametersMap).toString();
  }

  public String getClassificationTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getClassificationTypes(requestQueryParametersMap).toString();
  }

  public String getContributorTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getContributorTypes(requestQueryParametersMap).toString();
  }

  public String getContributorNameTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getContributorNameTypes(requestQueryParametersMap).toString();
  }

  public String getInstanceNoteTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getInstanceNoteTypes(requestQueryParametersMap).toString();
  }

  public String getAlternativeTitleTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getAlternativeTitleTypes(requestQueryParametersMap).toString();
  }

  public String getInventoryViewInstances(RequestQueryParameters requestQueryParameters, Boolean withBoundedItems) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getInventoryViewInstances(requestQueryParametersMap, withBoundedItems).toString();
  }

  public String getMaterialTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getMaterialTypes(requestQueryParametersMap).toString();
  }

  public String getMaterialTypeById(String materialTypeId) {
    return inventoryClient.getMaterialTypeById(materialTypeId).toString();
  }

  public String getSourceRecords(String id) {
    return inventoryClient.getSourceRecords(id).toString();
  }

  public String getAuthoritySourceRecords(String id) {
    return inventoryClient.getAuthoritySourceRecords(id).toString();
  }

  public String getAuthority(String authorityId) {
    return inventoryClient.getAuthority(authorityId).toString();
  }

  public String getAuthoritiesByQuery(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getAuthoritiesByQuery(requestQueryParametersMap).toString();
  }

  public String getSubjectSources(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getSubjectSources(requestQueryParametersMap).toString();
  }

  public String getSubjectTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getSubjectTypes(requestQueryParametersMap).toString();
  }

  public String getHoldingsNoteTypes(RequestQueryParameters requestQueryParameters) {
    var requestQueryParametersMap = requestQueryParametersMapper.toMap(requestQueryParameters);
    return inventoryClient.getHoldingsNoteTypes(requestQueryParametersMap).toString();
  }
}
