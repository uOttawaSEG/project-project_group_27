package com.example.otams_project;

public class Student extends User {


    String program;

    public Student(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Student() {
        super();
    }




    public static User register(String firstName, String lastName, String email , String password, String phone , String program) {
        Student student = new Student(firstName, lastName, phone);
        Account account = new Account(email, password, "student");
        student.program = program;
        student.account = account;

        return student;
    }
}
