package org.example.storeInventory;


import java.util.List;
import java.util.Map;

/**
 * @Author Issmale Bekri
 */

public class StoreInventorySystem implements StoreInventorySystemInterface {
    private final FilesProduct filesProduct;
    private String locationID;

    // Constructor that initializes the FilesProduct object
    public StoreInventorySystem(FilesProduct filesProduct) {
        this.filesProduct = filesProduct;
    }

    public void checkLowInventoryAcrossLocations() {
        for (Map.Entry<String, List<StoreUpdate>> entry : filesProduct.getAllInventoryData().entrySet()) {
            locationID = entry.getKey();
            List<StoreUpdate> storeUpdate = entry.getValue();
            alertLowInventory(storeUpdate);
        }
    }

    @Override
    public void alertLowInventory(List<StoreUpdate> productUpdates){
        for (StoreUpdate store : productUpdates) {
            if (store.getQuantity() < store.getReorderLevel()) {
                System.out.println("Alert: Low inventory for " + store.getProductName() +
                        " at quantity " + store.getQuantity() + " at location: " + locationID);
            }
        }
    }

    @Override
    public void warnInvalidUpdate(List<StoreUpdate> productUpdates){
        for (StoreUpdate store : productUpdates) {
            System.out.println("Warning: Invalid update for " + store.getProductName() +
                    " with attempted quantity " + store.getQuantity());
        }
    }

    // Update inventory for a specific location with a list of product updates
    @Override
    public boolean updateInventory(String locationID, List<StoreUpdate> productUpdates) {
        List<StoreUpdate> currentInventory = filesProduct.getInventoryData(locationID);

        if (currentInventory == null || currentInventory.isEmpty()) {
            System.out.println("No existing inventory found for location: " + locationID);
            return false;
        }

        for (StoreUpdate update : productUpdates) {
            boolean productFound = false;
            for (StoreUpdate existingUpdate : currentInventory) {
                if (existingUpdate.getProductID().equalsIgnoreCase(update.getProductID())) {
                    // Update the quantity and price for the existing product
                    int updatedQuantity = existingUpdate.getQuantity() + update.getQuantity();
                    if (updatedQuantity < 0) {
                        warnInvalidUpdate(List.of(update));
                        return false;
                    }
                    existingUpdate.setQuantity(updatedQuantity);
                    existingUpdate.setPrice(update.getPrice());  // Update the price
                    productFound = true;
                    break;
                }
            }

            if (!productFound) {
                System.out.println("Product with ID " + update.getProductID() + " not found for location " + locationID);
                return false;
            }
        }

        // Save the updated inventory
        filesProduct.saveInventoryData(locationID, currentInventory);
        return true;
    }
}
