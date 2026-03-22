package org.folio.edge.inventory.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class DataExportService {

  private final RestClient dataExportRestClient;

  /**
   * Downloads the authority data for a given authority ID.
   *
   * @param authorityId The unique identifier of the authority whose data is to be downloaded.
   * @param isUtf A boolean flag indicating whether the data should be encoded in UTF-8.
   * @return A ResponseEntity containing the downloaded authority data as a Resource.
   */
  public ResponseEntity<Resource> downloadAuthority(String authorityId, boolean isUtf, Boolean suppress999ff){
    return downloadRecord(authorityId, isUtf, "AUTHORITY", suppress999ff);
  }

  /**
   * Downloads the instance data for a given authority ID.
   *
   * @param instanceId The unique identifier of the instance whose data is to be downloaded.
   * @param isUtf A boolean flag indicating whether the data should be encoded in UTF-8.
   * @return A ResponseEntity containing the downloaded instance data as a Resource.
   */
  public ResponseEntity<Resource> downloadInstance(String instanceId, boolean isUtf){
    return downloadRecord(instanceId, isUtf, "INSTANCE", false);
  }

  private ResponseEntity<Resource> downloadRecord(String recordId, boolean isUtf, String idType, Boolean suppress999ff) {
    return dataExportRestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/download-record/{recordId}")
            .queryParam("utf", isUtf)
            .queryParam("idType", idType)
            .queryParam("suppress999ff", suppress999ff)
            .build(recordId))
        .exchange((request, response) -> ResponseEntity
            .status(response.getStatusCode())
            .headers(response.getHeaders())
            .body(new ByteArrayResource(response.getBody().readAllBytes())));
  }
}
