package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoggedInActivity extends AppCompatActivity {

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        account = LocalDataStorage.getAccount();
        Account newAccount = (Account) getIntent().getSerializableExtra("NewAccount");
        if (newAccount != null) {
            account = newAccount;
        }

        String roleToDisplay = "User";
        String welcomeMessage = "Account should not be null";
        String logoutButtonMessage = "Logout";
        String toastMessage = "Uh oh, something has gone wrong!";

        if (account != null) {
            if (!account.getRole().equals("admin")) {
                Button adminControlsButton = findViewById(R.id.adminButton);
                adminControlsButton.setVisibility(View.INVISIBLE);
            }

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
        if (LocalDataStorage.isLoginStatus()) {
            Toast.makeText(this, "You have been logged out of the " + LocalDataStorage.getAccount().getEmail() + " account", Toast.LENGTH_LONG).show();
            LocalDataStorage.getAccount().logout();
        }
        finish();
    }
    public void onAdminButtonClick(View view){
        if(account.getRole().equals("admin")){
            startActivity(new Intent(LoggedInActivity.this, AdminActivity.class));
        }
    }

    public void onResume() {
        super.onResume();
        if (account != LocalDataStorage.getAccount()) {
            if (!getIntent().getBooleanExtra("LogNewAccount", false)) {
                finish();
            } else {
                account.login();
            }
        }
    }
}