package com.example.otams_project;
import org.junit.Test;
import static org.junit.Assert.*;
public class UnitTests {

    //4 unit tests to verify basic functionality of our data classes

    @Test
    public void testSession(){
        String tutorEmail = "tutor@email.com";
        String studentEmail = "student@email.com";
        String date = "2025-12-25";
        String startTime = "12:00";
        String endTime = "13:00";
        String status = "pending";
        String courses = "SEG2105";
        Session session = new Session(tutorEmail, studentEmail, date, startTime, endTime, status,courses);

        assertEquals("Tutor email does not match" , tutorEmail, session.getTutorEmail());
        assertEquals("Student email does not match" , studentEmail, session.getStudentEmail());
        assertEquals("Date does not match" , date, session.getDate());
        assertEquals("Start time does not match" , startTime, session.getStartTime());
        assertEquals("End time does not match" , endTime, session.getEndTime());
        assertEquals("Status does not match" , status, session.getStatus());
        assertEquals("Courses does not match" , courses, session.getCourses());

    }

    @Test
    public void testAvailability() {
        String tutorEmail = "tutor@email.com";
        String date = "2025-12-25";
        String startTime = "12:00";
        String endTime = "13:00";
        boolean requiresApproval = true;
        boolean booked = false;
        String courses = "SEG2105";

        AvailabilitySlot availabilitySlot = new AvailabilitySlot(startTime, endTime, tutorEmail, date, requiresApproval, booked, courses);

        assertEquals("Tutor email does not match" , tutorEmail, availabilitySlot.getTutorEmail());
        assertEquals("Date does not match" , date, availabilitySlot.getDate());
        assertEquals("Start time does not match" , startTime, availabilitySlot.getStartTime());
        assertEquals("End time does not match" , endTime, availabilitySlot.getEndTime());
        assertEquals("Requires approval does not match" , requiresApproval, availabilitySlot.isRequiresApproval());
        assertEquals("Booked does not match" , booked, availabilitySlot.isBooked());
        assertEquals("Courses does not match" , courses, availabilitySlot.getCourses());

        assertEquals("Booked true when false" , false , availabilitySlot.isBooked());
        availabilitySlot.setBooked(true);
        assertEquals("Booked false when true" , true , availabilitySlot.isBooked());
    }

    @Test
    public void testAccount(){
        String email = "email@email.com";
        String password = "password";
        String role = "tutor";
        User user = new Tutor();
        Account account = new Account(email, password, role, user);

        assertEquals("Email does not match" , email, account.getEmail());
        assertEquals("Password does not match" , password, account.getPassword());
        assertEquals("Role does not match" , role, account.getRole());
        assertEquals("User does not match" , user, account.getUser());
        assertEquals("Status does not match expecring pending" , "pending", account.getStatus());
        account.setStatus("approved");
        assertEquals("Status does not match expecting approved" , "approved", account.getStatus());
        account.setStatus("rejected");
        assertEquals("Status does not match expecting rejected" , "rejected", account.getStatus());

    }

    @Test
    public void testUser(){

        String firstName = "firstName";
        String lastName = "lastName";
        String phone = "123-456-7890";

        User user = new User(firstName, lastName, phone);

        assertEquals("First name does not match" , firstName, user.getFirstName());
        assertEquals("Last name does not match" , lastName, user.getLastName());
        assertEquals("Phone does not match" , phone, user.getPhone());

    }

}
