package com.spring.reactive.registration.dto;

public class LoginDetails {
    private String userId;
    private String password;

    public LoginDetails() {
    }

    public LoginDetails(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDetails{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                '}';
    }
}