package org.example.equipmentMaintanence;

import java.io.*;
import java.util.*;

public class MaintenanceService implements MaintenanceInterface {
    private static final String TASKS_FILE = "tasks.csv";

    public MaintenanceService() {
        initializeTasksFile();
    }

    @Override
    public boolean scheduleMaintenance(String equipmentID, MaintenanceTask task) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            writer.write(task.getTaskID() + "," + equipmentID + "," + task.getScheduledTime() + "," + task.getIssueDescription() + "," + task.getAssignedTechnician());
            writer.newLine();
        }
        System.out.println("Scheduled maintenance for equipment: " + equipmentID);
        return true;
    }

    @Override
    public boolean completeMaintenance(String taskID, MaintenanceLog logDetails) throws IOException {
        List<String[]> tasks = readTasks();
        boolean found = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            writer.println("TaskID,EquipmentID,ScheduledTime,IssueDescription,AssignedTechnician"); // Header
            for (String[] task : tasks) {
                if (task[0].equals(taskID)) {
                    found = true;
                } else {
                    writer.println(String.join(",", task));
                }
            }
        }

        if (found) {
            logMaintenance(logDetails);
        }
        return found;
    }

    public MaintenanceTask getTask(String taskID) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts[0].equals(taskID)) {
                    MaintenanceTask task = new MaintenanceTask(parts[0], parts[1], parts[2], parts[3]);
                    task.setAssignedTechnician(parts[4].isEmpty() ? null : parts[4]);
                    return task;
                }
            }
        }
        return null;
    }

    @Override
    public boolean rescheduleMaintenance(String taskID, String newSchedule) throws IOException {
        return false;
    }

    @Override
    public boolean logIncompleteMaintenance(String taskID, String reason) throws IOException {
        return false;
    }

    private void logMaintenance(MaintenanceLog logDetails) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("logs.csv", true))) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    logDetails.getLogID(),
                    logDetails.getTaskID(),
                    logDetails.getCompletionDate(),
                    logDetails.getDescription(),
                    logDetails.getStatus());
        }
    }

    List<String[]> readTasks() throws IOException {
        List<String[]> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                tasks.add(line.split(",", -1));
            }
        }
        return tasks;
    }

    private void initializeTasksFile() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
                writer.println("TaskID,EquipmentID,ScheduledTime,IssueDescription,AssignedTechnician");
            } catch (IOException e) {
                System.err.println("Failed to initialize tasks file.");
                e.printStackTrace();
            }
        }
    }
}
