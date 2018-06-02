package com.mad.snapoverflow.model;

public class UsersSignupModel {
    public String username, email, password, university, areaOfInterest, dateOfBirth;

    public UsersSignupModel() {

    }

    public UsersSignupModel(String username, String email, String password, String university, String areaOfInterest, String dateOfBirth) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.university = university;
        this.areaOfInterest = areaOfInterest;
        this.dateOfBirth = dateOfBirth;
    }
}
