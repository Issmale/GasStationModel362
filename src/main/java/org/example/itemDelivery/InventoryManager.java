package org.example.itemDelivery;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager implements InventoryManagerInterface {
    private Map<String, InventoryItem> inventory;

    public InventoryManager() {
        this.inventory = new HashMap<>();
    }

    public void addItem(InventoryItem item) {
        if (inventory.containsKey(item.getId())) {
            System.out.println("Item already exists. Use updateItem to modify existing items.");
        } else {
            inventory.put(item.getId(), item);
            System.out.println("Added item: " + item);
        }
    }

    // Method to deliver items from a specific supplier
    public void deliverItems(String supplierName, Map<String, InventoryItem> deliveredItems) {
        System.out.println("Processing delivery from supplier: " + supplierName);
        for (Map.Entry<String, InventoryItem> entry : deliveredItems.entrySet()) {
            InventoryItem item = entry.getValue();
            addItem(item);
        }

        // Save this specific delivery to file
        saveInventoryToFile("inventory_log.txt", supplierName);
    }

    // Save inventory with supplier sections, appending to the file
    public void saveInventoryToFile(String filename, String supplierName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) { // Append mode
            writer.write("Supplier: " + supplierName + "\n"); // Supplier header
            for (InventoryItem item : inventory.values()) {
                writer.write("ID: " + item.getId() + ", Description: " + item.getDescription() +
                        ", Price: $" + item.getUnitPrice() + ", Quantity: " + item.getQuantity() + "\n");
            }
            writer.newLine(); // Blank line for readability
            System.out.println("Inventory appended to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }
}
