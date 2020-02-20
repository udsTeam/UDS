package com.maha.uds;

public class Babysitter {

    private String email;
    private String babysitterName ;
    private String userId;

    public Babysitter() {
    }

    public Babysitter(String email, String babysitterName, String userId) {
        this.email = email;
        this.babysitterName = babysitterName;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getBabysitterName() {
        return babysitterName;
    }

    public String getUserId() {
        return userId;
    }

}

