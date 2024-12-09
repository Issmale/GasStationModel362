package org.example.produceDocumentation;

import java.util.List;
import java.util.stream.Collectors;

public class ComplianceServiceImpl implements ComplianceService {
    private ComplianceRepository repository;

    public ComplianceServiceImpl(ComplianceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ComplianceRecord> getAllRecords() {
        return repository.loadAllRecords();
    }

    @Override
    public void saveComplianceRecord(ComplianceRecord record) {
        repository.saveRecord(record);
    }

    @Override
    public List<ComplianceRecord> detectMissingCompliance() {
        return repository.loadAllRecords().stream()
                .filter(record -> !record.isCompliant())
                .collect(Collectors.toList());
    }
}
