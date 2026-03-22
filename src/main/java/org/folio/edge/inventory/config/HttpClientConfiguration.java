package org.folio.edge.inventory.config;

import org.folio.edge.inventory.client.ConsortiaClient;
import org.folio.edge.inventory.client.InventoryClient;
import org.folio.edge.inventory.client.SearchClient;
import org.folio.edge.inventory.client.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfiguration {

  private final HttpServiceProxyFactory httpServiceProxyFactory;
  private final RestClient.Builder restClientBuilder;

  public HttpClientConfiguration(HttpServiceProxyFactory httpServiceProxyFactory, RestClient.Builder restClientBuilder) {
    this.httpServiceProxyFactory = httpServiceProxyFactory;
    this.restClientBuilder = restClientBuilder;
  }

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
