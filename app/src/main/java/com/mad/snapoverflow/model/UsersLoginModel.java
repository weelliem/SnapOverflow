package com.mad.snapoverflow.model;

public class UsersLoginModel {
    private String mEmail;
    private String mPassword;
    public String emailHint;
    public String passwordHint;

    public UsersLoginModel() {

    }

    public UsersLoginModel(String emailhint, String passwordhint) {
        this.emailHint = emailhint;
        passwordHint = passwordhint;
    }
}
