package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private final LocalDataStorage storage = LocalDataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalNotifier.initializeNotificationChannels(this);
    }
    public void onLoginClick(View view){
        if (storage.isLoginStatus()) {
            startActivity( new Intent(MainActivity.this , LoggedInActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
    public void onRegisterClick(View view){
        startActivity( new Intent(MainActivity.this , RegisterActivity.class));
    }
}