package com.maha.uds.Model;

public class ScheduleModel {

    private String day;
    private String date;
    private String time;

    public ScheduleModel() {
    }

    public ScheduleModel(String day, String date, String time) {
        this.day = day;
        this.date = date;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
