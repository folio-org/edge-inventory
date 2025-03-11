package org.folio.edge.inventory.service;


import static org.folio.edge.inventory.models.InventoryViewJsonFields.TOTAL_RECORDS;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.util.JsonConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcsLocationsService {

  public static final String LOCATIONS = "locations";
  public static final String LIBRARIES = "libraries";
  public static final String CAMPUSES = "campuses";
  public static final String INSTITUTIONS = "institutions";
  private final SearchClient searchClient;
  private final JsonConverter jsonConverter;

  public String getInstitutionById(String institutionId) {
    var locations = jsonConverter.readAsTree(searchClient.getConsortiumInstitutions(institutionId));
    if (locations.findValue(TOTAL_RECORDS).asInt() == 0) {
      throw new EntityNotFoundException("Institution with specified id not found: " + institutionId);
    }
    return locations.withObject("/"+ INSTITUTIONS +"/0").toString();
  }

  public String getLibraryById(String libraryId) {
    var locations = jsonConverter.readAsTree(searchClient.getConsortiumLibraries(libraryId));
    if (locations.findValue(TOTAL_RECORDS).asInt() == 0) {
      throw new EntityNotFoundException("Library with specified id not found: " + libraryId);
    }
    return locations.withObject("/"+ LIBRARIES +"/0").toString();
  }

  public String getCampusById(String campusId) {
    var locations = jsonConverter.readAsTree(searchClient.getConsortiumCampuses(campusId));
    if (locations.findValue(TOTAL_RECORDS).asInt() == 0) {
      throw new EntityNotFoundException("Campus with specified id not found: " + campusId);
    }
    return locations.withObject("/"+ CAMPUSES +"/0").toString();
  }

  public String getLocationById(String locationId) {
    var locations = jsonConverter.readAsTree(searchClient.getConsortiumLocations(locationId));
    if (locations.findValue(TOTAL_RECORDS).asInt() == 0) {
      throw new EntityNotFoundException("Location with specified id not found: " + locationId);
    }
    return locations.withObject("/"+ LOCATIONS +"/0").toString();
  }

  public String getConsortiumLocations() {
    return searchClient.getConsortiumLocations(null);
  }

}
