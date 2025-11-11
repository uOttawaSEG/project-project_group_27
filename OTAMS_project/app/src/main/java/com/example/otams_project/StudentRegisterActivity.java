package com.example.otams_project;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;



public class StudentRegisterActivity extends FieldValidatorActivity implements RegisterCallback {
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText phoneInput;
    private EditText programInput;

    private boolean submitted = false;  // prevent multiple clicks



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

        if(submitted) {
            return;
        }
        String firstName = firstNameInput.getText().toString().strip();
        String lastName = lastNameInput.getText().toString().strip();
        String email = emailInput.getText().toString().strip();
        String password = passwordInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String program = programInput.getText().toString().strip();

        if (this.isNameInvalid("First name", firstName))
            return;
        if (this.isNameInvalid("Last name", lastName))
            return;
        if (this.isPasswordInvalid(password))
            return;
        if (this.isPhoneNumberInvalid(phone))
            return;
        if (this.isEmailAddressInvalid(email))
            return;
        if (this.isInputInvalid("Program", program))
            return;

        submitted = true;

        Student.register(this, firstName, lastName, email, password, phone , program);


    }

    public void writeAccountFail() {
        submitted = false; //reset
        Toast.makeText(this, "Email already in use", Toast.LENGTH_LONG).show();
    }
    public void writeAccountSuccess() {
        submitted = false; //reset
        Toast.makeText(this, "Registration submitted for approval", Toast.LENGTH_LONG).show();
        startActivity( new Intent(StudentRegisterActivity.this , PendingApprovalActivity.class));
        finish();
    }

}


