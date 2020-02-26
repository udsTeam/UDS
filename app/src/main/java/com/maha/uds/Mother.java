package com.maha.uds;

public class Mother {
    private String motherName;
    private String childName;
    private String userId;
    private String email;


    public Mother() {
    }

    public Mother(String motherName, String childName, String email, String userId) {
        this.motherName = motherName;
        this.childName = childName;
        this.email = email;
        this.userId = userId;


    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotherName() {
        return motherName;
    }

    public String getChildName() {
        return childName;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
