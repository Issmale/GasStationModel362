package org.example.storeInventory;

import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface FilesProductInterface {
    boolean saveInventoryData(String locationID, List<StoreUpdate> productUpdates);
    boolean updateProduct(StoreProduct productDetails);
    List<StoreUpdate> getInventoryData(String locationID);
}
