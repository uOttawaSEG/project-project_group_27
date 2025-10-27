package com.example.otams_project;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//This class references many lines from the youtube video:
//How to send email using gmail SMTP server directly from your Android App?
//https://www.youtube.com/watch?v=JQRcT_m4tsA
//Further comments explain differences
//The build.gradle at the app level also referenced this video
//See said file for extra info
public class Emailer {

    //Address and passwords obviously must be different
    private final static String OTAMS_EMAIL_ADDRESS = "otams.noreply@gmail.com";
    //Gmail now requires an app password which is below
    private final static String OTAMS_PASSWORD = "diza xojr umdb mttb";
    private final static String emailHost = "smtp.gmail.com";
    //Used port 587 instead of 465 for educational exploration
    private final static String port = "587";

    public static void sendEmailForRegistrationStatus(Account account, boolean approve) {

        try {

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", emailHost);
            properties.put("mail.smtp.port", port);
            //587 port requires mail.smtp.starttls.enable instead of 465
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.auth", true);

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(OTAMS_EMAIL_ADDRESS, OTAMS_PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(account.getEmail()));

            //Added functionality to send different emails for approve/reject
            if (approve) {

                message.setSubject("OTAMS Account Approved!");
                message.setText("Hi " + account.getUser().getFirstName() + ", \n\n" +
                        "Your account has been approved, you can now login to OTAMS and use all of OTAMS's features as a " + account.getRole() + "!\n\n" +
                        "Happy studying,\nGroup 27.");

            } else {

                message.setSubject("OTAMS Account Rejected.");
                message.setText("Hi " + account.getUser().getFirstName() + ", \n\n" +
                        "Your account has been rejected. If you think this was a mistake login to see a number you can contact to see if we can help.\n\n" +
                        "Sincerely,\nGroup 27.");

            }

            //lambda was used to improve readability
            //this change was prompted by Android Studio
            //gets rid of the override and public void run() lines
            Thread thread = new Thread(() -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    //e.printStackTrace(); was originally used but
                    //it was more helpful to see errors with the Logcat
                    Log.d("Emailer","Exception when sending email");
                }
            });
            thread.start();

        } catch (AddressException e) {
            //same as above comment
            Log.d("Emailer","Address exception was thrown");
        } catch (MessagingException e) {
            //same as above comment
            Log.d("Emailer","Messaging exception was thrown");
        }

    }


}
