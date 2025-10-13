package com.example.otams_project;

public class Admin extends User{

    public Admin(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Admin() {
        super();
    }


    public static Account register(String firstName, String lastName, String email , String password, String phone) {
        Admin admin = new Admin(firstName, lastName, phone);
        Account account = new Account(email, password, "admin", admin);

        return account;
    }



}
