package org.example.itemDelivery;

import java.util.List;

public interface InventoryManagerInterface {
    boolean receiveGoods(String supplierID, List<InventoryItem> items);

}
