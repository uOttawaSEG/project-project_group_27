package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    Account tester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalNotifier.initializeNotificationChannels(this);
        tester = new Account("trevor.choi.tech@outlook.com", "pass", "role", new User("FirstName", "LastName", "Phone"));
    }
    public void onLoginClick(View view){
        LocalNotifier.sendRejectNotification(this, tester);
        Emailer.sendEmailForRegistrationStatus(tester, false);
        startActivity( new Intent(MainActivity.this , LoginActivity.class));
    }
    public void onRegisterClick(View view){
        LocalNotifier.sendApproveNotification(this, tester);
        Emailer.sendEmailForRegistrationStatus(tester, true);
        startActivity( new Intent(MainActivity.this , RegisterActivity.class));
    }
}