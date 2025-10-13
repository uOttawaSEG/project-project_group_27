package com.example.otams_project;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;


public class TutorRegisterActivity extends FieldValidatorActivity implements Register{
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
        String firstName = firstNameInput.getText().toString().strip();
        String lastName = lastNameInput.getText().toString().strip();
        String email = emailInput.getText().toString().strip();
        String password = passwordInput.getText().toString();
        String phone = phoneInput.getText().toString().strip();
        String degree = degreeInput.getText().toString().strip();
        String courses = coursesInput.getText().toString().strip();

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
        if(this.isInputInvalid("Highest degree", degree))
            return;
        if(this.isInputInvalid("Courses", courses))
            return;

        Account account = Tutor.register(firstName, lastName, email, password, phone , degree , courses);
        FirebaseAccessor accessor = new FirebaseAccessor();
        accessor.writeNewAccount(this, account);




    }

    public void writeAccountFail() {
        Toast.makeText(this, "Email already in use", Toast.LENGTH_LONG).show();
    }
    public void writeAccountSuccess() {
        Toast.makeText(this, "Successfully created tutor account", Toast.LENGTH_LONG).show();
        startActivity( new Intent(TutorRegisterActivity.this , MainActivity.class));
    }

}


