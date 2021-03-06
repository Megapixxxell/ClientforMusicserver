package com.example.clientformusicserver.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;

    private boolean mHasSuccessEmail;

    public User(String email, String name) {
        mEmail = email;
        mName = name;
    }

    public User(String email, String name, String password) {
        mEmail = email;
        mName = name;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean hasSuccessEmail() {
        return mHasSuccessEmail;
    }

    public void setHasSuccessEmail(boolean hasSuccessEmail) {
        mHasSuccessEmail = hasSuccessEmail;
    }
}
