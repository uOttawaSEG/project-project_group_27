package com.example.otams_project;

public class Tutor extends User {


    String degree;
    String courses;

    public Tutor(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Tutor() {
        super();
    }

    public String getDegree() {
        return degree;
    }

    public  String getCourses() {
        return courses;
    }

    @Override
    public String toFancyString() {
        return "Degree: " + degree + "\n" +
                "Courses: " + courses;
    }



    public static Account register(RegisterCallback context, String firstName, String lastName, String email , String password, String phone , String degree, String courses) {
        Tutor tutor = new Tutor(firstName, lastName, phone);
        Account account = new Account(email, password, "tutor", tutor);
        tutor.degree = degree;
        tutor.courses = courses;
        FirebaseAccessor accessor = FirebaseAccessor.getInstance();
        accessor.writeNewAccount(context, account);

        return account;
    }
}
