package org.example.safetyTraining;


import java.util.*;

public class MainSafe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SafetyLearningManager manager = new SafetyLearningManager();

        // Load existing employee data from CSV
        List<EmployeeTrainingRecord> employeeRecords = manager.loadEmployeeRecords();

        // Menu loop for interacting with the safety learning system
        while (true) {
            System.out.println("1. View all employees and their remaining training hours");
            System.out.println("2. Add new employee and assign training hours");
            System.out.println("3. Mark employee training as finished");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            if (choice == 1) {
                manager.viewEmployees(employeeRecords);
            } else if (choice == 2) {
                System.out.print("Enter employee name: ");
                String employeeName = scanner.nextLine();
                System.out.print("Enter role: ");
                String role = scanner.nextLine();
                System.out.print("Enter training hours to assign: ");
                double hours = scanner.nextDouble();
                scanner.nextLine();  // consume newline
                manager.addEmployee(employeeRecords, new EmployeeTrainingRecord(employeeName, role, hours));
                manager.saveEmployeeRecords(employeeRecords);
                System.out.println("Employee added and training assigned.\n");
            } else if (choice == 3) {
                System.out.print("Enter employee name to finish training: ");
                String employeeName = scanner.nextLine();
                boolean success = manager.finishTraining(employeeRecords, employeeName);
                if (success) {
                    manager.saveEmployeeRecords(employeeRecords);
                    System.out.println("Employee's training marked as finished and removed.\n");
                } else {
                    System.out.println("Employee not found or training not finished.\n");
                }
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.\n");
            }
        }

        scanner.close();
    }
}


interface SafetyLearningControllerInterface {
    boolean assignTraining(List<Employee> employees, List<TrainingModule> modules);
}

interface SafetyLearningSystemInterface {
    boolean processTraining(List<Employee> employees, List<TrainingModule> modules);
}



interface TrainingModuleInterface {
    String getName();
    double getDuration();
}


interface SafetyLearningManagerInterface {
    List<Employee> loadEmployeesFromCSV();
    void saveEmployeesToCSV(List<Employee> employees);
    void showAllEmployees(List<Employee> employees);
    void markTrainingComplete(List<Employee> employees, String employeeName);
    void addAdditionalTraining(List<Employee> employees, String employeeName, double additionalHours);
}

interface EmployeeInterface {
    String getName();
    double getTrainingRequired();
}
