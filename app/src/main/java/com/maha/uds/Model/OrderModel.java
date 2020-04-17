package com.maha.uds.Model;

import java.util.List;

public class OrderModel {

    private String motherID;
    private String babysitterID;
    private String babyID;
    private String paymentStatus;
    private DailyReportModel dailyReport;
    private String orderStatus;
    private int totalHours;
    private double price;
    private String orderDate;
    private List<ScheduleModel> scheduleList;

    public OrderModel() {
    }


    public OrderModel(String motherID, String babysitterID, String babyID, String paymentID, DailyReportModel dailyReportID, String orderStatus, String orderDate, int totalHours, double price, List<ScheduleModel> scheduleList) {
        this.motherID = motherID;
        this.babysitterID = babysitterID;
        this.babyID = babyID;
        this.paymentStatus = paymentID;
        this.orderDate = orderDate;
        this.dailyReport = dailyReportID;
        this.orderStatus = orderStatus;
        this.totalHours = totalHours;
        this.price = price;
        this.scheduleList = scheduleList;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getBabysitterID() {
        return babysitterID;
    }

    public void setBabysitterID(String babysitterID) {
        this.babysitterID = babysitterID;
    }

    public String getBabyID() {
        return babyID;
    }

    public void setBabyID(String babyID) {
        this.babyID = babyID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public DailyReportModel getDailyReport() {
        return dailyReport;
    }

    public void setDailyReport(DailyReportModel dailyReport) {
        this.dailyReport = dailyReport;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ScheduleModel> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleModel> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
