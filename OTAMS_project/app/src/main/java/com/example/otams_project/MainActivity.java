package com.example.otams_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAccessor database = new FirebaseAccessor();
        //Account account1 = new Account("example.gmail.com", "pass123", "student");
        //Account account2 = new Account("example5.gmail.com", "pass312", "tutor");
        //database.writeNewAccount(this, account1);
        //database.writeNewAccount(this, account2);

        database.doesEmailMatchPassword(this,"example.gmail.com","pass123");
    }

    public void approveSignIn(Account account) {
        Toast.makeText(this, account.getEmail()+ " has signed in, they are a " + account.getRole(), Toast.LENGTH_LONG).show();
    }

    public void denySignIn() {
        Toast.makeText(this, "Failed login", Toast.LENGTH_LONG).show();
    }

    public void writeAccountSuccess() {
        Toast.makeText(this, "Account creation successful", Toast.LENGTH_LONG).show();
    }

    public void writeAccountFail() {
        Toast.makeText(this, "Failed account creation", Toast.LENGTH_LONG).show();
    }
}