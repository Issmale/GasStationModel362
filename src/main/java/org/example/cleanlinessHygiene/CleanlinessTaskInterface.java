package org.example.cleanlinessHygiene;

public interface CleanlinessTaskInterface {

    void addTask(CleanlinessTask task);

    boolean markTaskComplete(String taskName, String completedBy);

    void generateReport();

    void remindMissedTasks();

    void escalateInspectionFailure(String taskName);
}
