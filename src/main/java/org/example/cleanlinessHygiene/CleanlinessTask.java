package org.example.cleanlinessHygiene;

public class CleanlinessTask {
    private String taskName;
    private String area;
    private String frequency;
    private boolean isCompleted;
    private String completedBy;

    // Constructor
    public CleanlinessTask(String taskName, String area, String frequency, String completedBy, boolean isCompleted) {
        this.taskName = taskName;
        this.area = area;
        this.frequency = frequency;
        this.isCompleted = isCompleted;
        this.completedBy = completedBy;
    }

    // Getters and setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }
}
