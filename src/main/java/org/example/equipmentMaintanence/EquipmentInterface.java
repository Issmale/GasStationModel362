package org.example.equipmentMaintanence;

import java.io.IOException;

public interface EquipmentInterface {
    Equipment getEquipmentDetails(String equipmentID) throws IOException;
    boolean updateEquipmentStatus(String equipmentID, String status) throws IOException;
    boolean addEquipment(Equipment equipment) throws IOException;
}
