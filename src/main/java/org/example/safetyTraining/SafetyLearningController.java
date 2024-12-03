package org.example.safetyTraining;

import java.util.List;

class SafetyLearningController implements SafetyLearningControllerInterface {
    private SafetyLearningSystem system;

    public SafetyLearningController() {
        system = new SafetyLearningSystem();
    }

    @Override
    public boolean assignTraining(List<Employee> employees, List<TrainingModule> modules) {
        return system.processTraining(employees, modules);
    }
}
