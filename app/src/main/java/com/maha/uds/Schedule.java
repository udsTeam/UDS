package com.maha.uds;

public class Schedule {

    private String Day;
    private String Date;
    private String Time;
    private String userId;

    public Schedule() {
    }

    public Schedule(String day, String date, String time,String userId) {
        this.Day = day;
        this.Date = date;
        this.Time = time;
        this.userId = userId;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getDate() {
        return Date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
