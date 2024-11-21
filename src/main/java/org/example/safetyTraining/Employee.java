package org.example.safetyTraining;

class Employee implements EmployeeInterface {
    private String name;
    private double trainingRequired;

    public Employee(String name, double trainingRequired) {
        this.name = name;
        this.trainingRequired = trainingRequired;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getTrainingRequired() {
        return trainingRequired;
    }

    public void setTrainingRequired(double trainingRequired) {
        this.trainingRequired = trainingRequired;
    }

    public void addTrainingHours(double hours) {
        this.trainingRequired += hours;
    }
}
