package org.example.safetyTraining;

import java.io.*;
import java.util.*;

public class SafetyLearningManager {

    private static final String FILE_PATH = "employee_training_records.csv";

    public List<EmployeeTrainingRecord> loadEmployeeRecords() {
        List<EmployeeTrainingRecord> employeeRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String role = parts[1];
                double hoursAssigned = Double.parseDouble(parts[2]);
                employeeRecords.add(new EmployeeTrainingRecord(name, role, hoursAssigned));
            }
        } catch (IOException e) {
            System.out.println("Error loading employee records.");
            e.printStackTrace();
        }
        return employeeRecords;
    }

    public void saveEmployeeRecords(List<EmployeeTrainingRecord> employeeRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (EmployeeTrainingRecord record : employeeRecords) {
                writer.write(record.getName() + "," + record.getRole() + "," + record.getAssignedHours() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving employee records.");
            e.printStackTrace();
        }
    }

    public void viewEmployees(List<EmployeeTrainingRecord> employeeRecords) {
        if (employeeRecords.isEmpty()) {
            System.out.println("No employees found.\n");
        } else {
            for (EmployeeTrainingRecord record : employeeRecords) {
                System.out.println(record.getName() + " - Role: " + record.getRole() + " - Hours Assigned: " + record.getAssignedHours());
            }
            System.out.println();
        }
    }

    public void addEmployee(List<EmployeeTrainingRecord> employeeRecords, EmployeeTrainingRecord newEmployee) {
        if (getEmployeeRecord(employeeRecords, newEmployee.getName()) == null) {
            employeeRecords.add(newEmployee);
        } else {
            System.out.println("Employee already exists.\n");
        }
    }

    public boolean finishTraining(List<EmployeeTrainingRecord> employeeRecords, String employeeName) {
        EmployeeTrainingRecord employee = getEmployeeRecord(employeeRecords, employeeName);
        if (employee != null && employee.getAssignedHours() > 0) {
            // Marking employee training as complete
            employee.setAssignedHours(0); // Mark training as completed
            employeeRecords.remove(employee); // Remove from the list
            return true;
        }
        return false;
    }

    private EmployeeTrainingRecord getEmployeeRecord(List<EmployeeTrainingRecord> employeeRecords, String name) {
        for (EmployeeTrainingRecord record : employeeRecords) {
            if (record.getName().equalsIgnoreCase(name)) {
                return record;
            }
        }
        return null;
    }
}
