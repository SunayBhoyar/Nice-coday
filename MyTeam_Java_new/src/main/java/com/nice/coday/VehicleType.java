package com.nice.coday;

public class VehicleType {
    private  String vehicleTypeName ; 
    private int numberOfUnitsForFullyCharge;
    private double mileage;

    public VehicleType(String vehicleType , int numberOfUnitsForFullyCharge_, double mileage_) {
        this.vehicleTypeName = vehicleType ; 
        this.numberOfUnitsForFullyCharge = numberOfUnitsForFullyCharge_;
        this.mileage = mileage_;
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
