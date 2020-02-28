package com.maha.uds.Model;

public class AccountModel {

    private String email;
    private String accountType;
    private String Name;
    private String bio;
    private String phone;
    private int ratting;
    private String age;
    private String status;


    public AccountModel() {
    }

    public AccountModel(String email, String accountType, String name, String bio, String phone, int ratting, String age, String status) {
        this.email = email;
        this.accountType = accountType;
        Name = name;
        this.bio = bio;
        this.phone = phone;
        this.ratting = ratting;
        this.age = age;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
