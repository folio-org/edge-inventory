package org.folio.edge.inventory.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.folio.spring.DefaultFolioExecutionContext;
import org.folio.spring.FolioExecutionContext;
import org.folio.spring.integration.XOkapiHeaders;

public final class FolioExecutionContextHelper {

  private FolioExecutionContextHelper() {
  }

  /**
   * Builds a copy of the given execution context with {@code x-okapi-tenant} replaced by the supplied
   * tenant, reusing every other header (token, url, request id, ...).
   *
   * <p>Used for ECS fan-out: running a client call inside {@code contextForTenant(ctx, member).execute(...)}
   * makes {@code EnrichUrlAndHeadersInterceptor} enrich the outgoing request with the member tenant taken
   * from the context, instead of the central tenant carried by the caller's context. This is the reason the
   * client interfaces no longer need per-call {@code x-okapi-tenant} header overloads.
   */
  public static FolioExecutionContext contextForTenant(FolioExecutionContext context, String tenantId) {
    Map<String, Collection<String>> headers = new HashMap<>();
    var sourceHeaders = context.getAllHeaders();
    if (sourceHeaders != null) {
      headers.putAll(sourceHeaders);
    }
    headers.keySet().removeIf(key -> key.equalsIgnoreCase(XOkapiHeaders.TENANT));
    headers.put(XOkapiHeaders.TENANT, List.of(tenantId));
    return new DefaultFolioExecutionContext(context.getFolioModuleMetadata(), headers);
  }
}
