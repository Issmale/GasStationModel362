package org.example.measureTanks;

import java.util.List;

public interface DataRepository {
    List<FuelTankRecord> loadAllRecords();
    void saveRecord(FuelTankRecord record);
}
