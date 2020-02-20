package com.maha.uds;

public class Mother {
    private String motherName;
    private String childName;
    private String userId;
    private String email;

    public Mother() {
    }

    public Mother(String motherName, String childName, String userId, String email) {
        this.motherName = motherName;
        this.childName = childName;
        this.userId = userId;
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
