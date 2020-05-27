package com.czerniecka.coronavirustracker.model;

public class RecoveryStats {

    private String state;
    private String country;
    private int recoveryStats;

    public int getDiffFromPreviousDay() {
        return diffFromPreviousDay;
    }

    public void setDiffFromPreviousDay(int diffFromPreviousDay) {
        this.diffFromPreviousDay = diffFromPreviousDay;
    }

    private int diffFromPreviousDay;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getRecoveryStats() {
        return recoveryStats;
    }

    public void setRecoveryStats(int recoveryStats) {
        this.recoveryStats = recoveryStats;
    }
}
