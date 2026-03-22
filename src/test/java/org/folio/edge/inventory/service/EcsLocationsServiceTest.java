package org.folio.edge.inventory.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.folio.edge.inventory.TestConstants;
import org.folio.edge.inventory.TestUtil;
import org.folio.edge.inventory.client.SearchClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EcsLocationsServiceTest {

  @Mock
  private SearchClient searchClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private EcsLocationsService ecsLocationsService;

  @BeforeEach
  void setUp() {
    ecsLocationsService = new EcsLocationsService(searchClient);
  }

  @Test
  @SneakyThrows
  void getInstitutionById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumInstitutions(TestConstants.INSTITUTION_ID))
        .thenReturn(getJsonNode(TestConstants.INSTITUTIONS_SEARCH_RESPONSE_PATH));
    var response = objectMapper.readTree(ecsLocationsService.getInstitutionById(TestConstants.INSTITUTION_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  @SneakyThrows
  void getInstitutionById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumInstitutions(TestConstants.INSTITUTION_ID))
        .thenReturn(getJsonNode(TestConstants.INSTITUTIONS_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getInstitutionById(TestConstants.INSTITUTION_ID));
  }

  @Test
  @SneakyThrows
  void getLocationById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumLocations(TestConstants.LOCATION_ID))
        .thenReturn(getJsonNode(TestConstants.LOCATIONS_SEARCH_RESPONSE_PATH));
    var response = objectMapper.readTree(ecsLocationsService.getLocationById(TestConstants.LOCATION_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  @SneakyThrows
  void getLocationById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumLocations(TestConstants.LOCATION_ID))
        .thenReturn(getJsonNode(TestConstants.LOCATIONS_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getLocationById(TestConstants.LOCATION_ID));
  }

  @Test
  @SneakyThrows
  void getCampusById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumCampuses(TestConstants.CAMPUS_ID))
        .thenReturn(getJsonNode(TestConstants.CAMPUSES_SEARCH_RESPONSE_PATH));
    var response = objectMapper.readTree(ecsLocationsService.getCampusById(TestConstants.CAMPUS_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  @SneakyThrows
  void getCampusById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumCampuses(TestConstants.CAMPUS_ID))
        .thenReturn(getJsonNode(TestConstants.CAMPUSES_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getCampusById(TestConstants.CAMPUS_ID));
  }

  @Test
  @SneakyThrows
  void getLibraryById_shouldReturnInstitutionFromModSearchById() {
    Mockito.when(searchClient.getConsortiumLibraries(TestConstants.LIBRARY_ID))
        .thenReturn(getJsonNode(TestConstants.LIBRARIES_SEARCH_RESPONSE_PATH));
    var response = objectMapper.readTree(ecsLocationsService.getLibraryById(TestConstants.LIBRARY_ID));
    assertTrue(response.hasNonNull("id"));
    assertTrue(response.hasNonNull("name"));
    assertTrue(response.hasNonNull("code"));
  }

  @Test
  @SneakyThrows
  void getLibraryById_shouldThrowEntityNotFoundWhenEmptyResponse() {
    Mockito.when(searchClient.getConsortiumLibraries(TestConstants.LIBRARY_ID))
        .thenReturn(getJsonNode(TestConstants.LIBRARIES_SEARCH_EMPTY_RESPONSE_PATH));
    Assertions.assertThrows(EntityNotFoundException.class, () -> ecsLocationsService.getLibraryById(TestConstants.LIBRARY_ID));
  }

  @SneakyThrows
  private tools.jackson.databind.JsonNode getJsonNode(String resourcePath) {
    return objectMapper.readTree(TestUtil.readFileContentFromResources(resourcePath));
  }
}
