package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        Account account = (Account) getIntent().getSerializableExtra("ACCOUNT");
        String roleToDisplay;
        if(account == null){
            roleToDisplay ="User";
        } else if (account.getRole() == null){
            roleToDisplay ="User";
        } else {
            roleToDisplay = account.getRole();
        }
        TextView welcomeText=findViewById(R.id.textView3);
        welcomeText.setText("Welcome! Successfully Logged in as " + roleToDisplay);




    }
    public void onLogoutButtonClick(View view){
        Account account = (Account) getIntent().getSerializableExtra("ACCOUNT");
        if (account != null) {
            account.logout();
        }
        startActivity( new Intent(LoggedInActivity.this , MainActivity.class));
        finish();
    }

}