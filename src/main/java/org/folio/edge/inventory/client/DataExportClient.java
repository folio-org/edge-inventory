package org.folio.edge.inventory.client;


import org.folio.edge.inventory.config.InventoryClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "data-export", configuration = InventoryClientConfig.class)
public interface DataExportClient {

  @GetMapping(value = "/data-export/download-record/{recordId}")
  ResponseEntity<Resource> downloadRecord(@PathVariable("recordId") String recordId, @RequestParam boolean utf,
      @RequestParam String idType);
}
