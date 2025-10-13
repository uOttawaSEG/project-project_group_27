package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;


public class TutorRegisterActivity extends AppCompatActivity implements Register{
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText phoneInput;
    private EditText degreeInput;

    private EditText coursesInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);

        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        degreeInput = findViewById(R.id.degreeInput);
        coursesInput = findViewById(R.id.coursesInput);
    }

    public void onRegisterButtonClick(View view){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String degree = degreeInput.getText().toString();
        String courses = coursesInput.getText().toString();
        String[] coursesArray = courses.split(",");
        User tutor = Tutor.register(firstName, lastName, email, password, phone , degree , coursesArray);
        FirebaseAccessor accessor = new FirebaseAccessor();
        accessor.writeNewAccount(this, tutor.getAccount());




    }

    public void writeAccountFail() {
        Toast.makeText(this, "Email already in use", Toast.LENGTH_LONG).show();
    }
    public void writeAccountSuccess() {
        Toast.makeText(this, "Successfully created tutor account", Toast.LENGTH_LONG).show();
        startActivity( new Intent(TutorRegisterActivity.this , MainActivity.class));
    }

}


