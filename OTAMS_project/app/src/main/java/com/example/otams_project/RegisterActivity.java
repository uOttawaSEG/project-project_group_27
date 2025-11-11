package com.example.otams_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    private final LocalDataStorage storage = LocalDataStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onStudentClick(View view){
        startActivity( new Intent(RegisterActivity.this , StudentRegisterActivity.class));
        if (storage.isLoginStatus()) {
            Toast.makeText(this, "You have been logged out of the " + storage.getAccount().getEmail() + " account", Toast.LENGTH_LONG).show();
            storage.getAccount().logout();
        }
    }
    public void onTutorClick(View view){
        startActivity( new Intent(RegisterActivity.this , TutorRegisterActivity.class));
        if (storage.isLoginStatus()) {
            Toast.makeText(this, "You have been logged out of the " + storage.getAccount().getEmail() + " account", Toast.LENGTH_LONG).show();
            storage.getAccount().logout();
        }
    }
}