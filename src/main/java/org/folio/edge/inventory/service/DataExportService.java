package org.folio.edge.inventory.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.edge.inventory.client.DataExportClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DataExportService {

  private final DataExportClient dataExportClient;

  /**
   * Downloads the authority data for a given authority ID.
   *
   * @param authorityId The unique identifier of the authority whose data is to be downloaded.
   * @param isUtf A boolean flag indicating whether the data should be encoded in UTF-8.
   * @return A ResponseEntity containing the downloaded authority data as a Resource.
   */
  public ResponseEntity<Resource> downloadAuthority(String authorityId, boolean isUtf){
    return dataExportClient.downloadRecord(authorityId, isUtf, "AUTHORITY");
  }

  /**
   * Downloads the instance data for a given authority ID.
   *
   * @param instanceId The unique identifier of the instance whose data is to be downloaded.
   * @param isUtf A boolean flag indicating whether the data should be encoded in UTF-8.
   * @return A ResponseEntity containing the downloaded instance data as a Resource.
   */
  public ResponseEntity<Resource> downloadInstance(String instanceId, boolean isUtf){
    return dataExportClient.downloadRecord(instanceId, isUtf, "INSTANCE");
  }
}
