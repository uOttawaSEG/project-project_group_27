package com.example.otams_project;

public interface LoginCallback {

    void denySignIn();
    void approveSignIn(Account account);
}
