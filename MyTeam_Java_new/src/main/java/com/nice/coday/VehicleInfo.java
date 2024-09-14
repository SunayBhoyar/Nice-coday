package com.nice.coday;

public class VehicleInfo {
    private int numberOfUnitsForFullyCharge;
    private double mileage;

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
    public String toString() {
        return "VehicleInfo{" +
               "numberOfUnitsForFullyCharge=" + numberOfUnitsForFullyCharge +
               ", mileage=" + mileage +
               '}';
    }
}
