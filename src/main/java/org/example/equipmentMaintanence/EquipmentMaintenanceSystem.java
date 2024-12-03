package org.example.equipmentMaintanence;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EquipmentMaintenanceSystem {
    private final EquipmentInterface equipmentService;
    private final MaintenanceInterface maintenanceService;

    public EquipmentMaintenanceSystem() {
        this.equipmentService = new EquipmentServiceImpl();
        this.maintenanceService = new MaintenanceService();
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Equipment Maintenance System");
            System.out.println("1. Add Equipment");
            System.out.println("2. Update Equipment Status");
            System.out.println("3. View Equipment Details");
            System.out.println("4. Schedule Maintenance");
            System.out.println("5. Complete Maintenance");
            System.out.println("6. Print All Equipment");
            System.out.println("7. Print All Maintenance Requests");
            System.out.println("8. Exit System");
            System.out.print("Select an option (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addEquipment(scanner);
                        break;
                    case 2:
                        updateEquipmentStatus(scanner);
                        break;
                    case 3:
                        viewEquipmentDetails(scanner);
                        break;
                    case 4:
                        scheduleMaintenance(scanner);
                        break;
                    case 5:
                        completeMaintenance(scanner);
                        break;
                    case 6:
                        printAllEquipment();
                        break;
                    case 7:
                        printAllMaintenanceRequests();
                        break;
                    case 8:
                        exit = true;
                        System.out.println("Exiting Equipment Maintenance System.");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a number between 1 and 8.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println(); // Add an empty line for readability
        }

        scanner.close();
    }

    private void addEquipment(Scanner scanner) throws IOException {
        System.out.print("Enter Equipment ID: ");
        String equipmentID = scanner.nextLine().trim();
        if (!validateID(equipmentID)) {
            System.out.println("Invalid Equipment ID format. Please use alphanumeric characters only.");
            return;
        }

        System.out.print("Enter Equipment Name: ");
        String name = scanner.nextLine().trim();

        Equipment equipment = new Equipment(equipmentID, name);
        if (equipmentService.addEquipment(equipment)) {
            System.out.println("Equipment added successfully.");
        } else {
            System.out.println("Failed to add equipment. It may already exist.");
        }
    }

    private void updateEquipmentStatus(Scanner scanner) throws IOException {
        System.out.print("Enter Equipment ID: ");
        String equipmentID = scanner.nextLine().trim();

        System.out.print("Enter New Status: ");
        String status = scanner.nextLine().trim();

        if (equipmentService.updateEquipmentStatus(equipmentID, status)) {
            System.out.println("Equipment status updated successfully.");
        } else {
            System.out.println("Failed to update status. Equipment ID not found.");
        }
    }

    private void viewEquipmentDetails(Scanner scanner) throws IOException {
        System.out.print("Enter Equipment ID: ");
        String equipmentID = scanner.nextLine().trim();

        Equipment equipment = equipmentService.getEquipmentDetails(equipmentID);
        if (equipment != null) {
            System.out.println("Equipment Details:");
            System.out.println("ID: " + equipment.getEquipmentID());
            System.out.println("Name: " + equipment.getName());
            System.out.println("Status: " + equipment.getStatus());
            System.out.println("Last Maintenance Date: " + (equipment.getLastMaintenanceDate() == null ? "N/A" : equipment.getLastMaintenanceDate()));
        } else {
            System.out.println("Equipment ID not found.");
        }
    }

    private void scheduleMaintenance(Scanner scanner) throws IOException {
        System.out.print("Enter Equipment ID: ");
        String equipmentID = scanner.nextLine().trim();
        if (equipmentService.getEquipmentDetails(equipmentID) == null) {
            System.out.println("Equipment ID not found. Please add the equipment first.");
            return;
        }

        System.out.print("Enter Task ID: ");
        String taskID = scanner.nextLine().trim();
        if (!validateID(taskID)) {
            System.out.println("Invalid Task ID format. Please use alphanumeric characters only.");
            return;
        }

        System.out.print("Enter Scheduled Time (YYYY-MM-DD): ");
        String scheduledTime = scanner.nextLine().trim();
        if (!validateDate(scheduledTime)) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter Issue Description: ");
        String issueDescription = scanner.nextLine().trim();

        MaintenanceTask task = new MaintenanceTask(taskID, equipmentID, scheduledTime, issueDescription);
        if (maintenanceService.scheduleMaintenance(equipmentID, task)) {
            System.out.println("Maintenance scheduled successfully.");
        } else {
            System.out.println("Failed to schedule maintenance. Task ID may already exist.");
        }
    }

    private void completeMaintenance(Scanner scanner) throws IOException {
        System.out.print("Enter Task ID: ");
        String taskID = scanner.nextLine().trim();

        System.out.print("Enter Log ID: ");
        String logID = scanner.nextLine().trim();
        if (!validateID(logID)) {
            System.out.println("Invalid Log ID format. Please use alphanumeric characters only.");
            return;
        }

        System.out.print("Enter Completion Date (YYYY-MM-DD): ");
        String completionDate = scanner.nextLine().trim();
        if (!validateDate(completionDate)) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter Maintenance Description: ");
        String description = scanner.nextLine().trim();

        MaintenanceLog log = new MaintenanceLog(logID, taskID, completionDate, description, "Completed");
        if (maintenanceService.completeMaintenance(taskID, log)) {
            updateEquipmentStatusAfterMaintenance(taskID);
            System.out.println("Maintenance completed successfully.");
        } else {
            System.out.println("Failed to complete maintenance. Task ID not found.");
        }
    }

    private void printAllEquipment() throws IOException {
        List<String[]> equipmentData = ((EquipmentServiceImpl) equipmentService).readEquipmentData();
        System.out.println("All Equipment:");
        for (String[] equipment : equipmentData) {
            System.out.println("ID: " + equipment[0] + ", Name: " + equipment[1] + ", Status: " + equipment[2] + ", Last Maintenance: " + equipment[3]);
        }
    }

    private void printAllMaintenanceRequests() throws IOException {
        List<String[]> tasks = ((MaintenanceService) maintenanceService).readTasks();
        System.out.println("All Maintenance Requests:");
        for (String[] task : tasks) {
            System.out.println("Task ID: " + task[0] + ", Equipment ID: " + task[1] + ", Scheduled Time: " + task[2] + ", Issue: " + task[3] + ", Technician: " + task[4]);
        }
    }

    private void updateEquipmentStatusAfterMaintenance(String taskID) throws IOException {
        MaintenanceTask task = ((MaintenanceService) maintenanceService).getTask(taskID);
        if (task != null) {
            equipmentService.updateEquipmentStatus(task.getEquipmentID(), "Operational");
        }
    }

    private boolean validateID(String id) {
        return Pattern.matches("^[a-zA-Z0-9]+$", id);
    }

    private boolean validateDate(String date) {
        return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", date);
    }
}
