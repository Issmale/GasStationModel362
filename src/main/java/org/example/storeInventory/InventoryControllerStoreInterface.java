package org.example.storeInventory;

import java.util.List;

public interface InventoryControllerStoreInterface {
    boolean manageProductInventory(String command, String locationID, List<StoreUpdate> productUpdates);

}
