package org.example.itemDelivery;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventorySystem implements InventorySystemInterface {
    private Map<String, Integer> inventoryRecords;

    public InventorySystem(InventoryManager inventoryManager) {
        inventoryRecords = new HashMap<>();
        loadInventoryFromFile();
    }
    private void loadInventoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line follows the format: Item ID: <itemID>, Received: <quantityReceived>, Expected: <quantityExpected>, Timestamp: <timestamp>
                String[] parts = line.split(",");
                String itemID = parts[0].split(":")[1].trim();
                int receivedQuantity = Integer.parseInt(parts[1].split(":")[1].trim());

                // Update the inventory map with loaded data
                inventoryRecords.put(itemID, inventoryRecords.getOrDefault(itemID, 0) + receivedQuantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<InventoryItem> getExpectedItems(String supplierID) {
        List<InventoryItem> expectedItems = new ArrayList<>();
        expectedItems.add(new InventoryItem("Item", "Description of Item1", 10, 5.0));
        return expectedItems;
    }

    @Override
    public boolean updateInventory(InventoryItem item, int quantity) {
        // Update the inventory records for the specified item
        int updatedQuantity = inventoryRecords.getOrDefault(item.getId(), 0) + quantity;
        inventoryRecords.put(item.getId(), updatedQuantity);
        recordInventoryUpdate(new InventoryUpdate(item.getId(), quantity, item.getQuantity()));
        return true;
    }

    @Override
    public void recordInventoryUpdate(InventoryUpdate inventoryUpdate) {
        System.out.println("Recorded inventory update for item " + inventoryUpdate.getItemID() +
                ": received " + inventoryUpdate.getQuantityReceived() +
                ", expected " + inventoryUpdate.getQuantityExpected());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_log.txt", true))) {
            writer.write("Item ID: " + inventoryUpdate.getItemID() +
                    ", Received: " + inventoryUpdate.getQuantityReceived() +
                    ", Expected: " + inventoryUpdate.getQuantityExpected() +
                    ", Timestamp: " + inventoryUpdate.getTimestamp() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printInventory() {
    }
}
