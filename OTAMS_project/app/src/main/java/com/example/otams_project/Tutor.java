package com.example.otams_project;

public class Tutor extends User {


    String degree;
    String[] courses;

    public Tutor(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Tutor() {
        super();
    }





    public static User register(String firstName, String lastName, String email , String password, String phone , String degree, String[] courses) {
        Tutor tutor = new Tutor(firstName, lastName, phone);
        Account account = new Account(email, password, "tutor");
        tutor.degree = degree;
        tutor.courses = courses;
        tutor.account = account;

        return tutor;
    }
}
