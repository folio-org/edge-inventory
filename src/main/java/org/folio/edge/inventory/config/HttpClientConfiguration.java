package org.folio.edge.inventory.config;

import lombok.RequiredArgsConstructor;
import org.folio.edge.inventory.client.ConsortiaClient;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UserClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfiguration {
  @Qualifier("edgeHttpServiceProxyFactory")
  private final HttpServiceProxyFactory httpServiceProxyFactory;
  private final RestClient.Builder restClientBuilder;

  @Bean
  public InventoryClient inventoryClient() {
    return httpServiceProxyFactory.createClient(InventoryClient.class);
  }

  @Bean
  public RestClient dataExportRestClient() {
    return restClientBuilder
        .baseUrl("data-export")
        .build();
  }

  @Bean
  public ConsortiaClient consortiaClient() {
    return httpServiceProxyFactory.createClient(ConsortiaClient.class);
  }

  @Bean
  public SearchClient searchClient() {
    return httpServiceProxyFactory.createClient(SearchClient.class);
  }

  @Bean
  public UserClient userClient() {
    return httpServiceProxyFactory.createClient(UserClient.class);
  }
}
