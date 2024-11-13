package org.folio.edge.inventory;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.folio.edge.inventory.config.InventoryClientRequestInterceptor;
import org.folio.edgecommonspring.client.EdgeFeignClientProperties;
import org.folio.edgecommonspring.client.EnrichUrlClient;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Log4j2
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({InventoryClientRequestInterceptor.class})
@TestPropertySource("classpath:test-application.yml")
@AutoConfigureMockMvc
@AutoConfigureObservability
public abstract class BaseIntegrationTests {

  protected static final WireMockServer WIRE_MOCK = new WireMockServer(
      WireMockSpring.options()
          .dynamicPort()
          .extensions(new ResponseTemplateTransformer(false)));
  private static final String TEST_API_KEY = "eyJzIjoiQlBhb2ZORm5jSzY0NzdEdWJ4RGgiLCJ0IjoidGVzdCIsInUiOiJ0ZXN0X2FkbWluIn0=";
  private static final String CENTRAL_API_KEY = "eyJzIjoiQlBhb2ZORm5jSzY0NzdEdWJ4RGgiLCJ0IjoiY2VudHJhbF90ZXN0IiwidSI6InRlc3RfYWRtaW4ifQ==";

  @Autowired
  protected MockMvc mockMvc;

  @BeforeAll
  static void beforeAll(@Autowired EnrichUrlClient enrichUrlClient,
      @Autowired InventoryClientRequestInterceptor interceptor, @Autowired EdgeFeignClientProperties properties) {
    WIRE_MOCK.start();
    var url = WIRE_MOCK.baseUrl();
    ReflectionTestUtils.setField(enrichUrlClient, TestConstants.OKAPI_URL_FIELD, url);
    ReflectionTestUtils.setField(interceptor, TestConstants.OKAPI_URL_FIELD, url);
    ReflectionTestUtils.setField(properties, TestConstants.OKAPI_URL_FIELD, url);
  }

  @AfterAll
  static void afterAll() {
    WIRE_MOCK.stop();
  }

  @SneakyThrows
  protected static ResultActions doGet(MockMvc mockMvc, String url) {
    return mockMvc.perform(get(url)
        .headers(defaultHeaders(false)));
  }

  @SneakyThrows
  protected static ResultActions doGet(MockMvc mockMvc, String url, boolean isCentral) {
    return mockMvc.perform(get(url)
        .headers(defaultHeaders(isCentral)));
  }

  @SneakyThrows
  protected static ResultActions doGetWithParams(MockMvc mockMvc, String url, String paramName, String paramValue) {
    return mockMvc.perform(get(url)
        .param(paramName, paramValue)
        .headers(defaultHeaders(false)));
  }

  @SneakyThrows
  protected static ResultActions doGetWithParams(MockMvc mockMvc, String url, String paramName, String paramValue, boolean isCentral) {
    return mockMvc.perform(get(url)
        .param(paramName, paramValue)
        .headers(defaultHeaders(isCentral)));
  }

  protected static HttpHeaders defaultHeaders(boolean isCentral) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(APPLICATION_JSON);
    httpHeaders.put(XOkapiHeaders.URL, List.of(WIRE_MOCK.baseUrl()));
    var apiKey = isCentral ? CENTRAL_API_KEY : TEST_API_KEY;
    httpHeaders.put(XOkapiHeaders.AUTHORIZATION, List.of(apiKey));
    return httpHeaders;
  }

}
