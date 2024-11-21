package org.example.safetyTraining;

public class EmployeeTrainingRecord {
    private String name;
    private String role;
    private double assignedHours;

    public EmployeeTrainingRecord(String name, String role, double assignedHours) {
        this.name = name;
        this.role = role;
        this.assignedHours = assignedHours;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getAssignedHours() {
        return assignedHours;
    }

    public void setAssignedHours(double assignedHours) {
        this.assignedHours = assignedHours;
    }
}
