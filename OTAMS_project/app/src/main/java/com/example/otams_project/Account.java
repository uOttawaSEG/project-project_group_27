package com.example.otams_project;

public class Account {
    private String email;
    private String password;
    private String role;

    public Account(String accountEmail, String accountPassword, String accountRole) {
        this.email = accountEmail;
        this.password = accountPassword;
        this.role = accountRole;
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

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
