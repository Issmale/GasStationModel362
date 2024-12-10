package org.example.foodAndBeverage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventorySystem implements IInventorySystem{
    private String inventoryFile;

    public InventorySystem(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }
    @Override
    public List<Item> retrieveInventory() {
        List<Item> inventory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                inventory.add(new Item(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    @Override
    public List<Item> reviewAndSelectItems(List<Item> inventory) {
        Scanner scanner = new Scanner(System.in);
        List<Item> selectedItems = new ArrayList<>();
        boolean selecting = true;

        // Display all inventory items
        System.out.println("Please select items for the menu (enter 'done' when finished):");
        System.out.println(String.format("%-20s %-15s %-15s", "Item Name", "Cost Price", "Selling Price"));
        System.out.println("--------------------------------------------------------");

        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            System.out.println(String.format("%d. %-20s %-15.2f %-15.2f", i + 1, item.getName(), item.getCostPrice(), item.getSellingPrice()));
        }

        while (selecting) {
            System.out.print("Enter the number of the item to select (or 'done' to finish): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("done")) {
                selecting = false;  // Exit the selection loop
            } else {
                try {
                    int itemIndex = Integer.parseInt(input) - 1;

                    if (itemIndex >= 0 && itemIndex < inventory.size()) {
                        Item selectedItem = inventory.get(itemIndex);
                        selectedItems.add(selectedItem);
                        System.out.println("Added " + selectedItem.getName() + " to the selected menu items.");
                    } else {
                        System.out.println("Invalid item number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid item number or 'done'.");
                }
            }
        }

        // Return the list of selected items
        return selectedItems;
    }



    @Override
    public void calculateCostAndProfit(List<Item> items) {
        double totalCost = 0.0;
        double totalProfit = 0.0;

        // Iterate through each item in the list
        for (Item item : items) {
            double cost = item.getCostPrice();
            double sellingPrice = item.getSellingPrice();

            // Calculate cost and profit for the item
            totalCost += cost;
            totalProfit += (sellingPrice - cost);

            // Display the cost and profit for each item
            System.out.println(String.format("Item: %-20s Cost: %.2f Selling Price: %.2f Profit: %.2f",
                    item.getName(), cost, sellingPrice, (sellingPrice - cost)));
        }

        // Display total cost and total profit
        System.out.println("-----------------------------------------------------------");
        System.out.println(String.format("Total Cost: %.2f", totalCost));
        System.out.println(String.format("Total Profit: %.2f", totalProfit));
    }


    @Override
    public void finalizeMenu(List<Item> items) {
        // Write selected items to menu.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu.csv"))) {
            for (Item item : items) {
                // Write item name and selling price to the file
                writer.write(String.format("%s,%.2f", item.getName(), item.getSellingPrice()));
                writer.newLine();
            }
            System.out.println("Menu finalized and saved to menu.csv.");
        } catch (IOException e) {
            System.err.println("Error writing to menu file: " + e.getMessage());
        }

        // Display the finalized menu
        displayMenu();
    }

    // Method to display the finalized menu
    private void displayMenu() {
        List<Item> menuItems = retrieveMenuItems(); // Assuming we have a method to retrieve the menu items

        System.out.println("\nFinalized Menu:");
        System.out.println("-------------------");  // This is the separator line

        // Display each item with its selling price
        for (Item item : menuItems) {
            System.out.println(String.format("%s, %.2f", item.getName(), item.getSellingPrice()));
        }
    }

    // Method to retrieve the menu items from menu.csv
    private List<Item> retrieveMenuItems() {
        List<Item> menuItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("menu.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    double sellingPrice = Double.parseDouble(parts[1].trim());
                    menuItems.add(new Item(name, 0.0, sellingPrice));  // Cost price is irrelevant here
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }

        return menuItems;
    }

}
