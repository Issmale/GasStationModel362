package org.example.storeLayout;

import java.io.*;
import java.util.*;

public class StoreLayout implements StoreLayoutInterface {
    public List<Shelf> shelves;

    // Constructor to load the store layout from CSV
    public StoreLayout(String csvFilePath) {
        shelves = new ArrayList<>();
        loadLayout(csvFilePath);
    }

    @Override
    // Load layout data from CSV into the shelves list
    public void loadLayout(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String shelfID = parts[0].trim();
                    String side = parts[1].trim();
                    String product = parts[2].trim();
                    int spaceAvailable = Integer.parseInt(parts[3].trim());
                    shelves.add(new Shelf(shelfID, side, product, spaceAvailable));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading layout CSV: " + e.getMessage());
        }
    }

    @Override
    // Display current store layout
    public void displayLayout() {
        for (Shelf shelf : shelves) {
            System.out.println(shelf);
        }
    }

    // Update the layout for a specific shelf
    @Override
    public boolean updateLayout(String shelfID, StoreLayout updatedLayout) {
        // Find the shelf by ID and update its properties
        for (Shelf shelf : shelves) {
            if (shelf.getShelfID().equals(shelfID)) {
                // Update the layout based on the new StoreLayout object
                Shelf updatedShelf = updatedLayout.getShelfByID(shelfID);
                if (updatedShelf != null) {
                    shelf.setProduct(updatedShelf.getProduct());
                    shelf.setSpaceAvailable(updatedShelf.getSpaceAvailable());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    // Get a shelf by ID
    public Shelf getShelfByID(String shelfID) {
        for (Shelf shelf : shelves) {
            if (shelf.getShelfID().equals(shelfID)) {
                return shelf;
            }
        }
        return null;
    }

    @Override
    // Save the updated layout back to the CSV file
    public void saveLayout(String csvFilePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write the header
            bw.write("ShelfID,Side,Product,SpaceAvailable\n");
            for (Shelf shelf : shelves) {
                bw.write(shelf.getShelfID() + "," + shelf.getSide() + "," + shelf.getProduct() + "," + shelf.getSpaceAvailable() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing layout CSV: " + e.getMessage());
        }
    }

    @Override
    public boolean isProductInStore(String product) {
        loadLayout("StoreLayout.csv");
        for (Shelf shelf : shelves) {
            if (shelf.getProduct().equalsIgnoreCase(product)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSideForProduct(String product) {
        loadLayout("StoreLayout.csv");
        for (Shelf shelf : shelves) {
            if (shelf.getProduct().equalsIgnoreCase(product)) {
                System.out.println(shelf.getProduct());
                System.out.println(shelf.getSide());
                return shelf.getSide();
            }
        }
        return null; // Return null if the product is not found
    }

    @Override
    public boolean addShelf(String shelfID, String side, String product, int spaceAvailable) {
        // Validate that the side is either "A" or "B"
        if (!side.equalsIgnoreCase("A") && !side.equalsIgnoreCase("B")) {
            System.out.println("Error: Side must be 'A' or 'B'.");
            return false;
        }

        // Check if the shelfID already exists
        for (Shelf shelf : shelves) {
            if (shelf.getShelfID().equalsIgnoreCase(shelfID)) {
                System.out.println("Error: A shelf with ID \"" + shelfID + "\" already exists.");
                return false;
            }
        }

        // Add the new shelf
        shelves.add(new Shelf(shelfID, side, product, spaceAvailable));
        System.out.println("Shelf added successfully!");
        return true;
    }


}
