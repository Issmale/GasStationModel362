package org.example.storeInventory;

import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface InventoryControllerStoreInterface {
    boolean manageProductInventory(String command, String locationID, List<StoreUpdate> productUpdates);

}
