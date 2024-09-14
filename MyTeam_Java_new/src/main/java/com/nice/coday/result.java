package com.nice.coday;

public class result {
    public double units;
    public long time;
    public long trips;
    public result(){
        units=0;
        time=0L;
        trips=0;
    }
    public result(double units, long time, long trips)
    {
        this.units=units;
        this.time=time;
        this.trips=trips;
    }
}
