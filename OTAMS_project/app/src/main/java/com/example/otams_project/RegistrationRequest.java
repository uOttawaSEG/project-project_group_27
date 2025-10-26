package com.example.otams_project;

public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String role;       //"Student" or "Tutor"
    private String degree;     //for Tutor
    private String courses;    //for Tutor
    private String program;    //for Student
    private String status;     //"pending","approved","rejected"

    public RegistrationRequest(String firstName, String lastName,
                               String email, String password,
                               String phone, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;

        this.status = "pending";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getDegree() {
        return degree;
    }

    public String getCourses() {
        return courses;
    }

    public String getProgram() {
        return program;
    }

    public String getStatus() {
        return status;
    }




    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
