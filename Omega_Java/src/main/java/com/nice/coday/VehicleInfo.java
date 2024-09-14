package com.nice.coday;

public class VehicleInfo {

    // Data members
    private int numberOfUnitsForFullyCharge;
    private double mileage;

    // Constructor
    public VehicleInfo(int numberOfUnitsForFullyCharge, double mileage) {
        this.numberOfUnitsForFullyCharge = numberOfUnitsForFullyCharge;
        this.mileage = mileage;
    }

    // Getters
    public int getNumberOfUnitsForFullyCharge() {
        return numberOfUnitsForFullyCharge;
    }

    public double getMileage() {
        return mileage;
    }

    @Override

    // Class methods
    public String toString() {
        return "VehicleInfo{" +
               "numberOfUnitsForFullyCharge=" + numberOfUnitsForFullyCharge +
               ", mileage=" + mileage +
               '}';
    }
}
