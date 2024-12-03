package org.example.cleanlinessHygiene;

import java.io.*;
import java.util.*;

public class CleanlinessManager implements CleanlinessManagerInterface, CleanlinessTaskInterface {

    private static final String FILE_PATH = "store_cleanliness_tasks.csv";

    // Load tasks from the CSV file
    public List<CleanlinessTask> loadTasks() {
        List<CleanlinessTask> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;  // Skip the header row
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and the header row
                if (line.trim().isEmpty() || isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");

                // Skip rows with incorrect number of columns
                if (parts.length != 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                String taskName = parts[0].trim();
                String area = parts[1].trim();
                String frequency = parts[2].trim();
                String completedBy = parts[3].trim();
                boolean isCompleted = Boolean.parseBoolean(parts[4].trim());

                // Add the task to the list
                tasks.add(new CleanlinessTask(taskName, area, frequency, completedBy, isCompleted));
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks.");
            e.printStackTrace();
        }
        return tasks;
    }


    // Add the task to the list
    public void saveTasks(List<CleanlinessTask> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Optionally, write header row if needed
            writer.write("Task Name, Area, Frequency, Completed By, Is Completed");
            writer.newLine();

            for (CleanlinessTask task : tasks) {
                // Make sure to handle any null or empty fields properly
                String taskName = task.getTaskName() != null ? task.getTaskName() : "";
                String area = task.getArea() != null ? task.getArea() : "";
                String frequency = task.getFrequency() != null ? task.getFrequency() : "";
                String completedBy = task.getCompletedBy() != null ? task.getCompletedBy() : "";
                boolean isCompleted = task.isCompleted();

                // Write the task details into the CSV in the correct order: taskName, area, frequency, completedBy, isCompleted
                writer.write(taskName + "," + area + "," + frequency + "," + completedBy + "," + isCompleted);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
            e.printStackTrace();
        }
    }



    // View all tasks
    @Override
    public void viewTasks() {
        List<CleanlinessTask> tasks = loadTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.\n");
        } else {
            System.out.println("----- Store Cleanliness Tasks -----");
            for (CleanlinessTask task : tasks) {
                System.out.println(task.getTaskName() + " - Area: " + task.getArea() + " - Frequency: "
                        + task.getFrequency() + " - Completed: " + task.isCompleted() + " - Completed by: "
                        + task.getCompletedBy());
            }
            System.out.println();
        }
    }

    // Add a new cleanliness task
    @Override
    public void addTask(CleanlinessTask task) {
        List<CleanlinessTask> tasks = loadTasks();
        tasks.add(task);
        saveTasks(tasks);
    }

    // Mark a task as complete
    @Override
    public boolean markTaskComplete(String taskName, String completedBy) {
        List<CleanlinessTask> tasks = loadTasks();
        CleanlinessTask task = getTask(tasks, taskName);
        if (task != null && !task.isCompleted()) {
            task.setCompleted(true);
            task.setCompletedBy(completedBy);
            saveTasks(tasks);
            return true;
        }
        return false;
    }

    // Generate a report of tasks that are still pending
    @Override
    public void generateReport() {
        List<CleanlinessTask> tasks = loadTasks();
        System.out.println("----- Cleanliness Task Report -----");
        for (CleanlinessTask task : tasks) {
            if (!task.isCompleted()) {
                System.out.println(task.getTaskName() + " - Area: " + task.getArea() + " - Frequency: "
                        + task.getFrequency());
            }
        }
    }

    // Generate a reminder for missed tasks
    @Override
    public void remindMissedTasks() {
        List<CleanlinessTask> tasks = loadTasks();
        System.out.println("----- Reminder for Missed Tasks -----");
        for (CleanlinessTask task : tasks) {
            if (!task.isCompleted()) {
                System.out.println("Reminder: " + task.getTaskName() + " is still pending.");
            }
        }
    }

    // Escalate a failed inspection task to a manager
    @Override
    public void escalateInspectionFailure(String taskName) {
        List<CleanlinessTask> tasks = loadTasks();
        CleanlinessTask task = getTask(tasks, taskName);
        if (task != null && !task.isCompleted()) {
            System.out.println("Escalation: Task '" + task.getTaskName() + "' failed inspection. Please take action.");
        }
    }

    // Helper method to get task by name
    private CleanlinessTask getTask(List<CleanlinessTask> tasks, String taskName) {
        for (CleanlinessTask task : tasks) {
            if (task.getTaskName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }
}
