package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;



public class StudentRegisterActivity extends AppCompatActivity implements Register {
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText phoneInput;
    private EditText programInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        programInput = findViewById(R.id.programInput);
    }

    public void onRegisterButtonClick(View view){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String program = programInput.getText().toString();

        User student = Student.register(firstName, lastName, email, password, phone , program);
        FirebaseAccessor accessor = new FirebaseAccessor();
        accessor.writeNewAccount(this, student.getAccount());


    }

    public void writeAccountFail() {
        Toast.makeText(this, "Email already in use", Toast.LENGTH_LONG).show();
    }
    public void writeAccountSuccess() {
        Toast.makeText(this, "Successfully created student account", Toast.LENGTH_LONG).show();
        startActivity( new Intent(StudentRegisterActivity.this , MainActivity.class));
    }
}


