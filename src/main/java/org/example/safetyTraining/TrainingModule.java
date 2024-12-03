package org.example.safetyTraining;

class TrainingModule implements TrainingModuleInterface {
    private String name;
    private double duration;

    public TrainingModule(String name, double duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDuration() {
        return duration;
    }
}
