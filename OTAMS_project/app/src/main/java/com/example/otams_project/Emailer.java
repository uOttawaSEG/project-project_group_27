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

public class Emailer {

    private final static String OTAMS_EMAIL_ADDRESS = "otams.noreply@gmail.com";
    //pls no hack
    private final static String OTAMS_PASSWORD = "diza xojr umdb mttb";
    private final static String emailHost = "smtp.gmail.com";
    private final static String port = "587";

    public static void sendEmailForRegistrationStatus(Account account, boolean approve) {
        try {

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", emailHost);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.auth", true);

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(OTAMS_EMAIL_ADDRESS, OTAMS_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(account.getEmail()));

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

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    Log.d("Emailer","Exception when sending email");
                }
            });
            thread.start();

        } catch (AddressException e) {
            Log.d("Emailer","Address exception was thrown");
        } catch (MessagingException e) {
            Log.d("Emailer","Messaging exception was thrown");
        }

    }


}
