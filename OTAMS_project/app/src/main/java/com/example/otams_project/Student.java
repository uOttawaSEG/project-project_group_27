package com.example.otams_project;

public class Student extends User {


    String program;

    public Student(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Student() {
        super();
    }

    public String getProgram() {
        return program;
    }

    @Override
    public String toFancyString() {
        return "Program: " + program;
    }


    public static Account register(Register context,String firstName, String lastName, String email , String password, String phone , String program) {
        Student student = new Student(firstName, lastName, phone);
        Account account = new Account(email, password, "student", student);
        student.program = program;
        FirebaseAccessor accessor = FirebaseAccessor.getInstance();
        accessor.writeNewAccount(context, account);

        return account;
    }

}
