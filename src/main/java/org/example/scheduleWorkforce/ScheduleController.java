package org.example.scheduleWorkforce;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScheduleController {

    private final DatabaseSupport databaseSupport;
    private final Scanner scanner;

    public ScheduleController(DatabaseSupport databaseSupport) {
        this.databaseSupport = databaseSupport;
        this.scanner = new Scanner(System.in);
    }

    // Main entry point for user interaction
    public void execute() {
        // Fetch the list of employee details from the database
        List<EmployeeInfo> employeeDetails = databaseSupport.fetchEmployeeDetails("Store001");

        // Main loop to keep the program running
        while (true) {
            // Display Employee IDs and Names
            System.out.println("Available Employees:");
            for (EmployeeInfo employee : employeeDetails) {
                System.out.println("EmployeeID: " + employee.getEmployeeID() + ", Name: " + employee.getEmployeeName());
            }

            // Ask if the user wants to schedule, manipulate availability, or exit
            System.out.print("\nDo you want to (1) schedule shifts, (2) manipulate availability, or (3) exit? (enter 1, 2, or 3): ");
            String actionChoice = scanner.nextLine().trim();

            // Exit the loop if the user chooses 3
            if (actionChoice.equals("3")) {
                System.out.println("Exiting the program.");
                break;
            }

            // Handle manipulate availability action
            if (actionChoice.equals("2")) {
                handleAvailabilityActions(employeeDetails);
            }
            // Handle schedule shifts action
            else if (actionChoice.equals("1")) {
                handleScheduleActions(employeeDetails);
            }
            else {
                System.out.println("Invalid choice. Please choose 1, 2, or 3.");
            }
        }
        scanner.close();
    }
    private void handleScheduleActions(List<EmployeeInfo> employeeDetails) {
        boolean backToMenu = false; // To track whether user wants to go back to the main menu after an action

        while (!backToMenu) {
            // Ask the user if they want to put, update, or remove a shift
            System.out.println("\nWhat would you like to do with the shift?");
            System.out.println("1. Put a new shift");
            System.out.println("2. Remove an existing shift");
            System.out.print("Enter your choice (1/2): ");
            String scheduleActionChoice = scanner.nextLine().trim();

            if (scheduleActionChoice.equals("1")) {
                // Ask for employee ID, day, and shift time
                System.out.print("Enter EmployeeID to schedule: ");
                String selectedEmployeeID = scanner.nextLine();

                System.out.print("Enter the day (e.g., Monday): ");
                String day = scanner.nextLine();

                System.out.print("Enter the shift time (e.g., 09:00-13:00): ");
                String shiftTime = scanner.nextLine();

                // Check if employee is available for the shift
                EmployeeAvailability employeeAvailability = new EmployeeAvailability("", "", "", "");
                boolean isAvailable = employeeAvailability.checkAvailability(selectedEmployeeID, day, shiftTime);

                if (isAvailable) {
                    // Get the available staff count for the shift
                    List<ShiftUpdate> existingSchedule = databaseSupport.getWorkScheduleData("Store001");

                    // Check for duplicates: Ensure the employee is not already scheduled for the same day and time
                    boolean isDuplicate = existingSchedule.stream().anyMatch(shift ->
                            shift.getDay().equalsIgnoreCase(day) &&
                                    shift.getShiftTime().equalsIgnoreCase(shiftTime) &&
                                    shift.getEmployeeID().equals(selectedEmployeeID)
                    );

                    if (isDuplicate) {
                        System.out.println("Error: Employee " + selectedEmployeeID + " is already scheduled for " + day + " at " + shiftTime + ".");
                    } else {
                        // Find the last instance of the matching shift for the specified day and shift time
                        ShiftUpdate existingShift = null;
                        for (int i = existingSchedule.size() - 1; i >= 0; i--) {
                            ShiftUpdate shift = existingSchedule.get(i);
                            if (shift.getDay().equalsIgnoreCase(day)) {
                                existingShift = shift;
                                break; // Stop once the last matching instance is found
                            }
                        }

                        if (existingShift != null) {
                            // Update staffing info: increment available staff, set staffing status
                            int newAvailableStaff = existingShift.getAvailableStaff() + 1;
                            String newStaffingStatus = (newAvailableStaff >= existingShift.getRequiredStaff()) ? "Fully Staffed" : "Understaffed";

                            // Associate the new employee with the shift
                            existingShift.setAvailableStaff(newAvailableStaff);
                            existingShift.setStaffingStatus(newStaffingStatus);
                            existingShift.setEmployeeID(selectedEmployeeID); // Set the EmployeeID
                            existingShift.setShiftTime(shiftTime);

                            // Add this shift to the schedule file
                            databaseSupport.putWorkScheduleData("Store001", Arrays.asList(existingShift));

                            System.out.println("Shift scheduled for Employee " + selectedEmployeeID + " on " + day + " from " + shiftTime);
                        }
                        else {
                            // Create a new shift entry
                            ShiftUpdate newShift = new ShiftUpdate(day, shiftTime, 4, 1, selectedEmployeeID, "Understaffed");

                            // Add this new shift to the schedule file
                            databaseSupport.putWorkScheduleData("Store001", Arrays.asList(newShift));

                            System.out.println("New shift created and scheduled for Employee " + selectedEmployeeID + " on " + day + " from " + shiftTime);
                        }
                    }
                } else {
                    System.out.println("Employee is not available for this shift.");
                }
            } else if (scheduleActionChoice.equals("2")) {
                // Remove shift
                System.out.print("Enter EmployeeID to remove: ");
                String employeeID = scanner.nextLine();

                System.out.print("Enter the day of the shift to remove: ");
                String day = scanner.nextLine();

                System.out.print("Enter the shift time (e.g., 09:00-13:00) or press Enter to skip: ");
                String shiftTime = scanner.nextLine();

                // Retrieve the current schedule
                List<ShiftUpdate> schedule = databaseSupport.getWorkScheduleData("Store001");

                // Find and remove matching shifts
                boolean shiftRemoved = schedule.stream()
                        .anyMatch(shift -> shift.getEmployeeID().equalsIgnoreCase(employeeID)
                                && shift.getDay().equalsIgnoreCase(day)
                                && (shiftTime.isEmpty() || shift.getShiftTime().equalsIgnoreCase(shiftTime)));

                if (shiftRemoved) {
                    boolean success = databaseSupport.removeEmployeeFromSchedule("Store001", employeeID, day);
                    if (success) {
                        System.out.println("Employee " + employeeID + " removed successfully.");
                    } else {
                        System.out.println("Failed to remove the shift.");
                    }
                } else {
                    System.out.println("No matching shift found.");
                }
            }

            // Ask if the user wants to go back to the main menu
            System.out.print("\nDo you want to go back to the main menu or continue with scheduling? (1 = main menu, 2 = continue): ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                backToMenu = true; // Exit the inner loop and return to the main menu
            }
        }
    }

    private void handleAvailabilityActions(List<EmployeeInfo> employeeDetails) {
        boolean backToMenu = false; // To track whether user wants to go back to the main menu after an action
        boolean newEmployee = false;
        String name;
        String number;

        while (!backToMenu) {
            // Ask if the user wants to add or remove availability
            System.out.print("\nDo you want to add or remove availability for an existing employee? (add/remove): ");
            String availabilityActionChoice = scanner.nextLine().trim().toLowerCase();

            String selectedEmployeeID;
            String selectedEmployeeName = "";
            String selectedEmployeeContact = "";

            // For adding or removing availability
            if (availabilityActionChoice.equals("add") || availabilityActionChoice.equals("remove")) {
                System.out.print("Enter EmployeeID to " + availabilityActionChoice + " availability: ");
                selectedEmployeeID = scanner.nextLine();

                // Check if the employee exists in the list
                EmployeeInfo selectedEmployee = employeeDetails.stream()
                        .filter(employee -> employee.getEmployeeID().equals(selectedEmployeeID))
                        .findFirst()
                        .orElse(null);

                if (selectedEmployee != null) {
                    selectedEmployeeName = selectedEmployee.getEmployeeName();
                    selectedEmployeeContact = selectedEmployee.getEmployeeContactInfo();
                    System.out.println("Selected Employee: " + selectedEmployeeName);
                } else {
                    System.out.println("EmployeeID " + selectedEmployeeID + " not found.");
                    newEmployee = true;
                }

                // Ask for day and shift time
                System.out.print("Enter the day (e.g., Monday): ");
                String day = scanner.nextLine();

                System.out.print("Enter the shift time (e.g., 09:00-13:00): ");
                String shiftTime = scanner.nextLine();

                // Create an EmployeeAvailability instance
                EmployeeAvailability ea = new EmployeeAvailability("", "", "", "");

                boolean result = false;
                if (availabilityActionChoice.equals("add")) {
                    // Call the addAvailability method if adding availability
                    if(newEmployee){
                        System.out.print("Enter the employee name: ");
                        name = scanner.nextLine();
                        System.out.print("Enter the employee contact info: ");
                        number = scanner.nextLine();
                        result = ea.addAvailability(selectedEmployeeID, day, shiftTime, name, number);
                    }
                    else{
                        result = ea.addAvailability(selectedEmployeeID, day, shiftTime, selectedEmployeeName, selectedEmployeeContact);
                    }
                } else if (availabilityActionChoice.equals("remove")) {
                    // Call the removeAvailability method if removing availability
                    result = ea.removeAvailability(selectedEmployeeID, day, shiftTime);
                }

                // Display the result
                if (result) {
                    if (availabilityActionChoice.equals("add")) {
                        System.out.println("\nAvailability for EmployeeID " + selectedEmployeeID + " has been added for " + day + " from " + shiftTime);
                    } else if (availabilityActionChoice.equals("remove")) {
                        System.out.println("\nAvailability for EmployeeID " + selectedEmployeeID + " has been removed for " + day + " from " + shiftTime);
                    }
                } else {
                    System.out.println("\nFailed to " + availabilityActionChoice + " availability for EmployeeID " + selectedEmployeeID + ".");
                }
            } else {
                System.out.println("Invalid action. Please choose 'add' or 'remove'.");
            }

            // Ask if the user wants to go back to the main menu
            System.out.print("\nDo you want to go back to the main menu or continue with availability? (1 = main menu, 2 = continue): ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                backToMenu = true; // Exit the inner loop and return to the main menu
            }
        }
    }

}
