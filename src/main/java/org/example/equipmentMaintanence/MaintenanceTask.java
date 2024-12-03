package org.example.equipmentMaintanence;

public class MaintenanceTask {
    private String taskID;
    private String equipmentID;
    private String scheduledTime;
    private String issueDescription;
    private String assignedTechnician;

    // Constructor
    public MaintenanceTask(String taskID, String equipmentID, String scheduledTime, String issueDescription) {
        this.taskID = taskID;
        this.equipmentID = equipmentID;
        this.scheduledTime = scheduledTime;
        this.issueDescription = issueDescription;
    }

    // Getters and Setters
    public String getTaskID() {
        return taskID;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setScheduledTime(String newSchedule) {
        this.scheduledTime = newSchedule;
    }
}
