package com.example.otams_project;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

//This class references many lines from the youtube video:
//Local Notifications in Android - The Full Guide (Android Studio Tutorial)
//https://www.youtube.com/watch?v=LP623htmWcI&t
//Further comments explain differences
public class LocalNotifier {

    private static final String REQUEST_STATUS_CHANNEL_ID = "requestChannel";
    private static NotificationManager manager;

    //The code in this method was originally placed in MainActivity but has been moved to separate code
    public static void initializeNotificationChannels(Context caller) {
        //Version is checked here since Android Studio shows an error without
        //Minimum build version is Nougat, less than Oreo
        //This line is not different from the video but is worth mentioning since it requires
        //more knowledge than how to use the API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(REQUEST_STATUS_CHANNEL_ID, "Request Status", IMPORTANCE_HIGH);
            channel.setDescription("Notifies you when your account request status changes.");

            NotificationManager notificationManager = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);
            manager = notificationManager;

            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void sendRequestStatusNotification(Context caller, Account account, boolean approve) {

        //Null check required since initializeNotificationChannels is not guaranteed to run first
        if (manager == null) {
            return;
        }

        //Almost unique code so different accounts don't replace each-other's notifications
        //Same code for approve and reject so if rejected then approved, the reject notification
        //is overwritten
        int requestCode = account.getEmail().hashCode();

        Intent openApp = new Intent(caller, MainActivity.class);
        Intent adminContactIntent = new Intent(caller, RegisterActivity.class);

        //No check for build version since it should always be above Marshmallow
        PendingIntent openAppPending = PendingIntent.getActivity(caller, requestCode, openApp, PendingIntent.FLAG_IMMUTABLE);

        //Additional pendingIntent so user navigate to admin number with button on the notification
        PendingIntent adminContactPendingIntent = PendingIntent.getActivity(caller, requestCode, adminContactIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(caller, REQUEST_STATUS_CHANNEL_ID);

        notificationBuilder.setContentTitle("Request Status Update");

        String notificationText;

        //Different notification settings for approve and deny
        if (approve) {
            //No addAction() required for approved
            notificationText = account.getUser().getFirstName() + " your account has been approved!";
            notificationBuilder.setSmallIcon(R.drawable.outline_check_24);
        } else {
            //Instead of an increment button, user can navigate to admin number
            notificationText = account.getUser().getFirstName() + " your account has been rejected.";
            notificationBuilder.setSmallIcon(R.drawable.outline_close_24);
            notificationBuilder.addAction(R.drawable.outline_android_24, "Contact Admin", adminContactPendingIntent);
        }

        notificationBuilder.setContentText(notificationText);
        notificationBuilder.setContentIntent(openAppPending);

        manager.notify(requestCode, notificationBuilder.build());
    }
}
