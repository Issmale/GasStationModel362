package org.example.cleanlinessHygiene;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CleanlinessManager manager = new CleanlinessManager();

        while (true) {
            System.out.println("1. View Tasks");
            System.out.println("2. Add Task");
            System.out.println("3. Mark Task Complete");
            System.out.println("4. Generate Report");
            System.out.println("5. Reminder for Missed Tasks");
            System.out.println("6. Escalate Inspection Failure");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    manager.viewTasks();
                    break;
                case 2:
                    System.out.print("Enter task name: ");
                    String taskName = scanner.nextLine();
                    System.out.print("Enter task area: ");
                    String area = scanner.nextLine();
                    System.out.print("Enter task frequency: ");
                    String frequency = scanner.nextLine();
                    CleanlinessTask newTask = new CleanlinessTask(taskName, area, frequency, "", false);
                    manager.addTask(newTask);
                    break;
                case 3:
                    System.out.print("Enter task name to complete: ");
                    String completeTaskName = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String completedBy = scanner.nextLine();
                    if (manager.markTaskComplete(completeTaskName, completedBy)) {
                        System.out.println("Task marked as completed.");
                    } else {
                        System.out.println("Task not found or already completed.");
                    }
                    break;
                case 4:
                    manager.generateReport();
                    break;
                case 5:
                    manager.remindMissedTasks();
                    break;
                case 6:
                    System.out.print("Enter task name to escalate: ");
                    String escalateTask = scanner.nextLine();
                    manager.escalateInspectionFailure(escalateTask);
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}


