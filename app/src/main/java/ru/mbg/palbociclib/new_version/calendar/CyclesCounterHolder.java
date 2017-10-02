package ru.mbg.palbociclib.new_version.calendar;

public class CyclesCounterHolder {

    private static CyclesCounterHolder sInstance;

    private double mCycleCounter = -1;

    private CyclesCounterHolder(){

    }

    public static CyclesCounterHolder instance(){
        if (sInstance == null){
            sInstance = new CyclesCounterHolder();
        }
        return sInstance;
    }

    public void setCycleCounter(double cycleCounter){
        mCycleCounter = cycleCounter;
    }

    public double getCycleCounter() {
        return mCycleCounter;
    }

    private double cycle;

    public double increase(){
        cycle = cycle + 0.1001;
        return cycle;
    }

    public void reset(double increase){
        cycle = mCycleCounter - increase;
    }

}
