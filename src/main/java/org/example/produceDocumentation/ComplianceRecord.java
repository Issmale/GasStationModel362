package org.example.produceDocumentation;

public class ComplianceRecord {
    private String regulationId;
    private String description;
    private boolean isCompliant;

    public ComplianceRecord(String regulationId, String description, boolean isCompliant) {
        this.regulationId = regulationId;
        this.description = description;
        this.isCompliant = isCompliant;
    }

    public String getRegulationId() {
        return regulationId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompliant() {
        return isCompliant;
    }

    @Override
    public String toString() {
        return "RegulationID: " + regulationId + ", Description: " + description + ", Compliant: " + isCompliant;
    }
}
