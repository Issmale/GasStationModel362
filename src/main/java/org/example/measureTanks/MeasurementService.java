package org.example.measureTanks;

import java.util.List;
import java.util.Optional;

public interface MeasurementService {
    Optional<FuelTankRecord> getHistoricalRecord(String tankId);
    void saveMeasurement(FuelTankRecord record);
    double calculateDifference(FuelTankRecord historicalRecord, FuelTankRecord newRecord);
    List<FuelTankRecord> getAllRecords();
    double predictFuelLevel(String tankId); // New method
}
