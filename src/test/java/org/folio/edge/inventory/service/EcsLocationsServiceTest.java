package org.folio.edge.inventory.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.folio.edge.inventory.TestConstants;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.util.JsonConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EcsLocationsServiceTest {

  @Mock
  private SearchClient searchClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final JsonConverter jsonConverter = new JsonConverter(objectMapper);
  private EcsLocationsService ecsLocationsService;

  @BeforeEach
  void setUp() {
    ecsLocationsService = new EcsLocationsService(searchClient, jsonConverter);
  }


  @Test
  void getInstitutionById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumInstitutions(TestConstants.INSTITUTION_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.INSTITUTIONS_SEARCH_RESPONSE_PATH));
    var response = jsonConverter.readAsTree(ecsLocationsService.getInstitutionById(TestConstants.INSTITUTION_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  void getInstitutionById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumInstitutions(TestConstants.INSTITUTION_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.INSTITUTIONS_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getInstitutionById(TestConstants.INSTITUTION_ID));
  }

  @Test
  void getLocationById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumLocations(TestConstants.LOCATION_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.LOCATIONS_SEARCH_RESPONSE_PATH));
    var response = jsonConverter.readAsTree(ecsLocationsService.getLocationById(TestConstants.LOCATION_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  void getLocationById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumLocations(TestConstants.LOCATION_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.LOCATIONS_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getLocationById(TestConstants.LOCATION_ID));
  }

  @Test
  void getCampusById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumCampuses(TestConstants.CAMPUS_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.CAMPUSES_SEARCH_RESPONSE_PATH));
    var response = jsonConverter.readAsTree(ecsLocationsService.getCampusById(TestConstants.CAMPUS_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  void getCampusById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumCampuses(TestConstants.CAMPUS_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.CAMPUSES_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getCampusById(TestConstants.CAMPUS_ID));
  }

  @Test
  void getLibraryById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumLibraries(TestConstants.LIBRARY_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.LIBRARIES_SEARCH_RESPONSE_PATH));
    var response = jsonConverter.readAsTree(ecsLocationsService.getLibraryById(TestConstants.LIBRARY_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  void getLibraryById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumLibraries(TestConstants.LIBRARY_ID))
        .thenReturn(TestUtil.readFileContentFromResources(TestConstants.LIBRARIES_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getLibraryById(TestConstants.LIBRARY_ID));
  }

}
