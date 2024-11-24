package org.example.scheduleWorkforce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EmployeeAvailability implements EmployeeAvailabilityInterface{

    private String employeeID;
    private String day;
    private String shiftTime;
    private String availabilityStatus;

    public EmployeeAvailability(String employeeID, String day, String shiftTime, String availabilityStatus) {
        this.employeeID = employeeID;
        this.day = day;
        this.shiftTime = shiftTime;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and setters
    public String getEmployeeID() {
        return employeeID;
    }

    public String getDay() {
        return day;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
    @Override
    public boolean addAvailability(String employeeID, String day, String shiftTime, String employeeName, String employeeContact) {
        String newAvailability = employeeID + "," + employeeName + "," + employeeContact + "," + day + "," + shiftTime + ",Available";
        boolean isAddedOrUpdated = false;

        try {
            // Read the existing data from the file
            List<String> lines = Files.readAllLines(Paths.get("EmployeeSchedule.csv"));


            // Check if the employee exists in the file
            boolean employeeExists = lines.stream().anyMatch(line -> line.startsWith(employeeID + ","));

            if (employeeExists) {
                // Check if the availability already exists for the employee on the same day and shift
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    // Split the line into components
                    String[] components = line.split(",");

                    // Check for exact match on employeeID, day, and shiftTime
                    if (components[0].equals(employeeID) && components[3].equals(day)) {
                        // Replace the old availability with the new one
                        lines.set(i, newAvailability);
                        System.out.println("Updated availability: " + newAvailability);
                        isAddedOrUpdated = true;
                        break;
                    }
                }

            } else {
                // For new employees, add a full new entry
                lines.add(newAvailability);
                System.out.println("Adding new availability: " + newAvailability);
                isAddedOrUpdated = true;
            }

            // Write the updated list back to the file if any changes were made
            if (isAddedOrUpdated) {
                Files.write(Paths.get("EmployeeSchedule.csv"), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Successfully updated the schedule.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading or writing the file.");
        }

        return isAddedOrUpdated;
    }

    @Override
    public boolean removeAvailability(String employeeID, String day, String shiftTime) {
        boolean isUpdated = false;

        try {
            // Read the existing data from the file
            List<String> lines = Files.readAllLines(Paths.get("EmployeeSchedule.csv"));

            // Check if the employee exists in the file
            boolean employeeExists = lines.stream().anyMatch(line -> line.startsWith(employeeID + ","));

            if (employeeExists) {
                // Iterate over the lines to find the matching availability record
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] components = line.split(",");

                    // Check for exact match on employeeID, day, and shiftTime
                    if (components[0].equals(employeeID) && components[3].equals(day)) {
                        // Change availability status from "Available" to "Unavailable"
                        components[5] = "Unavailable";  // Set the status to "Unavailable"
                        lines.set(i, String.join(",", components));  // Update the line in the list
                        isUpdated = true;
                        break;
                    }
                }

                // If no matching availability was found, it will remain false (i.e., no update was made)
                if (!isUpdated) {
                    System.out.println("No matching availability found for the given shift to update.");
                }
            } else {
                System.out.println("Employee with ID " + employeeID + " not found.");
            }

            // Write the updated list back to the file if any changes were made
            if (isUpdated) {
                Files.write(Paths.get("EmployeeSchedule.csv"), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Successfully updated the schedule after marking the shift as Unavailable.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading or writing the file.");
        }

        return isUpdated;
    }


    @Override
    public boolean checkAvailability(String employeeID, String day, String shiftTime) {
        DatabaseSupport databaseSupport = new DatabaseSupport();
        List<EmployeeAvailability> availabilityList = databaseSupport.getEmployeeAvailability("Store001");

        // Split the input shift time into start and end times
        String[] inputShiftRange = shiftTime.split("-");
        int inputStart = convertTimeToMinutes(inputShiftRange[0]);
        int inputEnd = convertTimeToMinutes(inputShiftRange[1]);

        // Filter and check for overlapping time ranges
        return availabilityList.stream()
                .filter(ea -> ea.getEmployeeID().equals(employeeID)
                        && ea.getDay().equalsIgnoreCase(day)
                        && ea.getAvailabilityStatus().equalsIgnoreCase("Available"))
                .anyMatch(ea -> {
                    String[] shiftRange = ea.getShiftTime().split("-");
                    int shiftStart = convertTimeToMinutes(shiftRange[0]);
                    int shiftEnd = convertTimeToMinutes(shiftRange[1]);
                    // Check if the input time range is within the available time range
                    return inputStart >= shiftStart && inputEnd <= shiftEnd;
                });
    }

    /**
     * Converts a time string (HH:mm) to total minutes since midnight.
     */
    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

}
