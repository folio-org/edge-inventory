package org.folio.edge.inventory.service.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import org.folio.inventory.domain.dto.RequestQueryParameters;
import org.springframework.stereotype.Component;

@Component
public class RequestQueryParametersMapper {

  public Map<String, ?> toMap(RequestQueryParameters p) {
    Map<String, Object> map = new LinkedHashMap<>();
    if (p == null) {
      return map;
    }

    if (p.getQuery() != null && !p.getQuery().isBlank()) {
      map.put("query", p.getQuery());
    }
    if (p.getLimit() != null) {
      map.put("limit", p.getLimit());
    }
    if (p.getOffset() != null) {
      map.put("offset", p.getOffset());
    }
    if (p.getLang() != null && !p.getLang().isBlank()) {
      map.put("lang", p.getLang());
    }
    if (p.getExpand() != null && !p.getExpand().isBlank()) {
      map.put("expand", p.getExpand());
    }

    return map;
  }
}

