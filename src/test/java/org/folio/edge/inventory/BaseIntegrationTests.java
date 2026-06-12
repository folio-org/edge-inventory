package org.folio.edge.inventory;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.folio.edgecommonspring.client.EdgeClientProperties;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTests {

  protected static final WireMockServer WIRE_MOCK = new WireMockServer(
      options()
          .dynamicPort()
          .templatingEnabled(true)
          .globalTemplating(false));
  private static final String TEST_API_KEY = "eyJzIjoiQlBhb2ZORm5jSzY0NzdEdWJ4RGgiLCJ0IjoidGVzdCIsInUiOiJ0ZXN0X2FkbWluIn0=";
  private static final String CENTRAL_API_KEY = "eyJzIjoiQlBhb2ZORm5jSzY0NzdEdWJ4RGgiLCJ0IjoiY2VudHJhbF90ZXN0IiwidSI6InRlc3RfYWRtaW4ifQ==";

  @Autowired
  protected MockMvc mockMvc;

  @BeforeAll
  static void beforeAll(@Autowired EdgeClientProperties edgeClientProperties) {
    WIRE_MOCK.start();
    ReflectionTestUtils.setField(edgeClientProperties, TestConstants.OKAPI_URL_FIELD, WIRE_MOCK.baseUrl());
    log.info("Wire mock started");
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
