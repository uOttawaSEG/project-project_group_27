package com.example.otams_project;

import java.io.Serializable;

public class User implements Serializable{
    private String firstName;
    private String lastName;
    private String phone;

    protected User(String firstName, String lastName, String phone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

    }
    public User(){
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
    }


    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhone(){
        return phone;
    }

    public String toFancyString() {
        return "";
    }


    public static Account register(String firstName, String lastName, String email , String password, String phone){
        User user = new User(firstName, lastName, phone);
        Account account = new Account(email, password, "null", user);
        FirebaseAccessor accessor = new FirebaseAccessor();

        return account;
    }
}
