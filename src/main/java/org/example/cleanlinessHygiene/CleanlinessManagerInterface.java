package org.example.cleanlinessHygiene;

import java.util.List;

public interface CleanlinessManagerInterface {

    List<CleanlinessTask> loadTasks();

    void saveTasks(List<CleanlinessTask> tasks);

    void addTask(CleanlinessTask task);

    void viewTasks();

    boolean markTaskComplete(String taskName, String completedBy);

    void generateReport();

    void remindMissedTasks();

    void escalateInspectionFailure(String taskName);
}