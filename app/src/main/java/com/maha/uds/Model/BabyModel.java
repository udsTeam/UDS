package com.maha.uds.Model;

public class BabyModel {

    private String name;
    private String gender;
    private String age;
    private String babyNotes;
    private String motherID;


    public BabyModel() {
    }

    public BabyModel(String name, String gender, String age, String babyNotes, String motherID) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.babyNotes = babyNotes;
        this.motherID = motherID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBabyNotes() {
        return babyNotes;
    }

    public void setBabyNotes(String babyNotes) {
        this.babyNotes = babyNotes;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }
}