package org.example.measureTanks;

import java.util.List;
import java.util.Optional;

public class MeasurementServiceImpl implements MeasurementService {
    private DataRepository repository;

    public MeasurementServiceImpl(DataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FuelTankRecord> getHistoricalRecord(String tankId) {
        List<FuelTankRecord> records = repository.loadAllRecords();
        return records.stream()
                .filter(record -> record.getTankId().equals(tankId))
                .findFirst();
    }

    @Override
    public void saveMeasurement(FuelTankRecord record) {
        repository.saveRecord(record);
    }

    @Override
    public double calculateDifference(FuelTankRecord historicalRecord, FuelTankRecord newRecord) {
        return newRecord.getFuelLevel() - historicalRecord.getFuelLevel();
    }

    @Override
    public List<FuelTankRecord> getAllRecords() {
        return repository.loadAllRecords();
    }

    @Override
    public double predictFuelLevel(String tankId) {
        List<FuelTankRecord> records = repository.loadAllRecords();
        double totalFuel = 0;
        int count = 0;

        for (FuelTankRecord record : records) {
            if (record.getTankId().equals(tankId)) {
                totalFuel += record.getFuelLevel();
                count++;
            }
        }

        // Prediction: Use average fuel level and deduct estimated daily consumption
        double averageFuel = count > 0 ? totalFuel / count : 0.0;
        double estimatedDailyConsumption = 10.0; // Example fixed value
        return averageFuel - estimatedDailyConsumption; // Predict for the next day
    }
}
