package org.folio.edge.inventory.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes implements ErrorAttributes {

  private static final String DEFAULT_KEY_STATUS = "status";
  private static final String DEFAULT_KEY_MESSAGE = "message";
  private static final String KEY_STATUS = "code";
  private static final String KEY_MESSAGE = "errorMessage";

  private final DefaultErrorAttributes delegate = new DefaultErrorAttributes();

  @Override
  public Throwable getError(WebRequest webRequest) {
    return delegate.getError(webRequest);
  }

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

    Map<String, Object> defaultErrorAttributes = delegate.getErrorAttributes(webRequest, options);

    Map<String, Object> errorAttributes = new LinkedHashMap<>();
    errorAttributes.put(KEY_STATUS, defaultErrorAttributes.get(DEFAULT_KEY_STATUS));
    errorAttributes.put(KEY_MESSAGE, defaultErrorAttributes.get(DEFAULT_KEY_MESSAGE));

    return errorAttributes;
  }

}
