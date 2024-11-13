package org.folio.edge.inventory.models;

import java.util.List;

public record InstanceTenants(String instanceId, List<String> tenantIds) {

}
