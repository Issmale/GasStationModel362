package org.example.itemDelivery;

import java.util.List;

public interface InventoryControllerInterface {
    boolean processDelivery(String supplierID, List<InventoryItem> items);
}
