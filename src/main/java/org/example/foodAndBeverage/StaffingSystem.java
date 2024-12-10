package org.example.foodAndBeverage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StaffingSystem implements IStaffingSystem {
    private String staffingFile;

    // Constructor to initialize the staffing file path
    public StaffingSystem(String staffingFile) {
        this.staffingFile = staffingFile;
    }

    // Method to check if there are enough staff available
    @Override
    public boolean checkStaffingLevels() {
        int availableStaff = loadStaffingData();

        // Check if the available staff is enough (at least 2 people)
        if (availableStaff >= 2) {
            return true; // Sufficient staff
        } else {
            System.out.println("Insufficient staff. You need at least 2 staff members.");
            return false; // Insufficient staff
        }
    }

    // Helper method to load the staffing data from the file
    private int loadStaffingData() {
        int staffCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(staffingFile))) {
            String line = reader.readLine(); // Read the first line
            if (line != null) {
                staffCount = Integer.parseInt(line.trim()); // Convert the line to an integer
            }
        } catch (IOException e) {
            System.err.println("Error reading staffing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: The staffing file content is not a valid number.");
        }

        return staffCount;
    }

}
