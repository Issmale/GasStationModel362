package org.example.safetyTraining;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class SafetyLearningSystem implements SafetyLearningSystemInterface {
    private static final String FILE_PATH = "safety_learning_data.csv";

    @Override
    public boolean processTraining(List<Employee> employees, List<TrainingModule> modules) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Employee Name,Required Training Hours,Assigned Modules\n");

            for (Employee employee : employees) {
                writer.write(employee.getName() + "," + employee.getTrainingRequired() + ",");

                double remainingHours = employee.getTrainingRequired();
                StringBuilder assignedModules = new StringBuilder();

                for (TrainingModule module : modules) {
                    if (remainingHours <= 0) break;

                    if (module.getDuration() <= remainingHours) {
                        assignedModules.append(module.getName()).append(" ");
                        remainingHours -= module.getDuration();
                    }
                }

                writer.write(assignedModules.toString().trim() + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
