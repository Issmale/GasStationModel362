package org.example.equipmentMaintanence;

public class MaintenanceLog {
    private String logID;
    private String taskID;
    private String completionDate;
    private String description;
    private String status;

    // Constructor
    public MaintenanceLog(String logID, String taskID, String completionDate, String description, String status) {
        this.logID = logID;
        this.taskID = taskID;
        this.completionDate = completionDate;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
    public String getLogID() {
        return logID;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
