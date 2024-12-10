package org.example.produceDocumentation;

import java.util.List;

public interface ComplianceService {
    List<ComplianceRecord> getAllRecords();
    void saveComplianceRecord(ComplianceRecord record);
    List<ComplianceRecord> detectMissingCompliance();
}
