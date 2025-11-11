package com.example.otams_project;

import android.util.Log;

import java.io.Serializable;

public class User implements Serializable{
    private String firstName;
    private String lastName;
    private String phone;
    private Account account;

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
    public Account getAccount(boolean notDatabase) {
        return account;
    }
    public void setAccount(Account account) {
        if (account.getUser() == this) {
            this.account = account;
        } else {
            Log.d("User","Tried to set user's account to an account which does not contain this user");
        }
    }

    public String toFancyString() {
        return "";
    }


    public static Account register(RegisterCallback context, String firstName, String lastName, String email , String password, String phone){
        User user = new User(firstName, lastName, phone);
        Account account = new Account(email, password, "null", user);
        FirebaseAccessor accessor = FirebaseAccessor.getInstance();
        accessor.writeNewAccount(context, account);

        return account;
    }
}
