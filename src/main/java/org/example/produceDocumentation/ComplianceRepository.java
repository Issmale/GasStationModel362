package org.example.produceDocumentation;

import java.util.List;

public interface ComplianceRepository {
    List<ComplianceRecord> loadAllRecords();
    void saveRecord(ComplianceRecord record);
}
