package com.mad.snapoverflow.model;

public class UsersLoginModel {
    private String mEmail;
    private String mPassword;
    public String mEmailhint;
    public String mPasswordhint;

    public UsersLoginModel() {

    }

    public UsersLoginModel(String emailhint, String passwordhint) {
        mEmailhint = emailhint;
        mPasswordhint = passwordhint;
    }
}
