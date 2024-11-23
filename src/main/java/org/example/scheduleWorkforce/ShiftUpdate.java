package org.example.scheduleWorkforce;

public class ShiftUpdate implements ShiftUpdateInterface{
    @Override
    public boolean updateShift(String storeID, Shift shift) {
        return false;
    }

    @Override
    public boolean removeShift(String storeID, String shiftID) {
        return false;
    }

    @Override
    public boolean resolveShiftConflict(String storeID, Shift shift) {
        return false;
    }

    @Override
    public boolean addShift(String storeID, Shift shift) {
        return false;
    }
}
