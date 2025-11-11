package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoggedInActivity extends AppCompatActivity {

    Account account;
    private final LocalDataStorage storage = LocalDataStorage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        Button adminControlsButton = findViewById(R.id.adminButton);
        Button tutorButton = findViewById(R.id.tutorButton);

        adminControlsButton.setVisibility(View.INVISIBLE);
        tutorButton.setVisibility(View.INVISIBLE);

        account = storage.getAccount();
        Account newAccount = (Account) getIntent().getSerializableExtra("NewAccount");
        if (newAccount != null) {
            account = newAccount;
        }
/*
        if (account.getRole().equals("tutor")) {
            Toast.makeText(this, ((Tutor)account.getUser()).toFancyString(), Toast.LENGTH_LONG).show();
        }

 */


        String roleToDisplay = "User";
        String welcomeMessage = "Account should not be null";
        String logoutButtonMessage = "Logout";
        String toastMessage = "Uh oh, something has gone wrong!";

        if (account != null) {

            if (account.getRole() != null) {
                roleToDisplay = account.getRole();
            }

            if (account.getStatus() == null) {


                welcomeMessage = "You have a legacy " + roleToDisplay + " account, contact 123-456-7890 for help";
                toastMessage = "Our apologies " + account.getUser().getFirstName();


            } else {


                switch (account.getStatus()) {
                    case "approved":
                        welcomeMessage = "Welcome! Successfully Logged in as " + roleToDisplay;
                        toastMessage = account.getEmail() + " has signed in, they are a(n) " + account.getRole();

                        switch (roleToDisplay) {
                            case "admin":
                                adminControlsButton.setVisibility(View.VISIBLE);
                                break;
                            case "tutor":
                                tutorButton.setVisibility(View.VISIBLE);
                                break;
                            case "student":
                                //TO ADD student button
                                break;
                            default:
                                //Unexpected role
                                Log.d("LoggedInRole","Unexpected role in logged in account");
                                break;
                        }

                        break;
                    case "pending":
                        welcomeMessage = "Your registration for the " + roleToDisplay + " role is pending approval";
                        toastMessage = "Hang in there " + account.getUser().getFirstName() + "!";
                        break;
                    case "rejected":
                        welcomeMessage = "Your registration for the " + roleToDisplay + " role has been rejected, please contact 123-456-7890 for help";
                        toastMessage = account.getEmail() + " has attempted to sign in, they are a(n) " + account.getRole();
                        break;
                    default:
                        welcomeMessage = "Registration status is an unexpected value";
                        break;
                }


            }

        }

        TextView welcomeText=findViewById(R.id.textView3);
        welcomeText.setText(welcomeMessage);

        Button logoutButton = findViewById(R.id.logoutInput);
        logoutButton.setText(logoutButtonMessage);

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();


    }
    public void onLogoutButtonClick(View view){
        if (storage.isLoginStatus()) {
            Toast.makeText(this, "You have been logged out of the " + storage.getAccount().getEmail() + " account", Toast.LENGTH_LONG).show();
            storage.getAccount().logout();
        }
        finish();
    }
    public void onAdminButtonClick(View view){
        if(account.getRole().equals("admin")){
            startActivity(new Intent(LoggedInActivity.this, AdminActivity.class));
        }
    }

    public void onTutorButtonClick(View view) {
        if (account.getRole().equals("tutor")) {
            startActivity(new Intent(LoggedInActivity.this, TutorActivity.class));
        }
    }

    public void onResume() {
        super.onResume();
        if (account != storage.getAccount()) {
            if (!getIntent().getBooleanExtra("LogNewAccount", false)) {
                finish();
            } else {
                account.login();
            }
        }
    }
}