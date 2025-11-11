package com.example.otams_project;

import java.io.Serializable;

public class Account implements Serializable {
    private String email;
    private String password;
    private String role;
    private String status;
    private User user;
    private final LocalDataStorage storage = LocalDataStorage.getInstance();

    public Account(String accountEmail, String accountPassword, String accountRole, User accountUser) {
        this.email = accountEmail;
        this.password = accountPassword;
        this.role = accountRole;
        this.user = accountUser;
        this.status = "pending";
    }

    public Account() {

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    public User getUser() {
        return  user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String toString() {
        return " Email: " + email + " Role: " + role + " Status: " + status;
    }

    public String toFancyString() {
        return "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + user.getPhone() + "\n" +
                "Role: " + role + "\n" +
                user.toFancyString();
    }



    public void login() {
        storage.setAccount(this);
    }

    public void logout() {
        storage.setAccount(new Account());
    }
}
