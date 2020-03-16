package com.maha.uds.Model;

public class DailyReportModel {

    private String arriveTime;
    private String leavingTime;
    private String napTime;
    private String mealReport;
    private String UnusualNotes;



    public DailyReportModel(String arriveTime, String leavingTime, String napTime, String mealReport, String unusualNotes) {
        this.arriveTime = arriveTime;
        this.leavingTime = leavingTime;
        this.napTime = napTime;
        this.mealReport = mealReport;
        UnusualNotes = unusualNotes;
    }

    public DailyReportModel() {
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(String leavingTime) {
        this.leavingTime = leavingTime;
    }

    public String getNapTime() {
        return napTime;
    }

    public void setNapTime(String napTime) {
        this.napTime = napTime;
    }

    public String getMealReport() {
        return mealReport;
    }

    public void setMealReport(String mealReport) {
        this.mealReport = mealReport;
    }

    public String getUnusualNotes() {
        return UnusualNotes;
    }

    public void setUnusualNotes(String unusualNotes) {
        UnusualNotes = unusualNotes;
    }

    @Override
    public String toString() {
        return "DailyReportModel{" +
                "arriveTime='" + arriveTime + '\'' +
                ", leavingTime='" + leavingTime + '\'' +
                ", napTime='" + napTime + '\'' +
                ", mealReport='" + mealReport + '\'' +
                ", UnusualNotes='" + UnusualNotes + '\'' +
                '}';
    }
}
