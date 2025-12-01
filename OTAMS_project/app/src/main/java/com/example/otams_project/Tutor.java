package com.example.otams_project;

public class Tutor extends User {


    String degree;
    String courses;

    private Float rating;
    private Integer numRatings;

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

    public Float getRating() {return rating;}
    public Integer getNumRatings() {return numRatings;}

    public void addRating(int newRating, String email, StudentActivity activity, Session session) {
        if (numRatings == null || rating == null) {
            numRatings = 0;
            rating = (float)0;
        }

        this.rating = ((rating*numRatings) + newRating)/(numRatings + 1);
        this.numRatings = numRatings + 1;
        FirebaseAccessor.getInstance().updateRating(this.rating, this.numRatings, email, activity, session);
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
