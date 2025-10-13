package com.example.otams_project;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FieldValidatorActivity extends AppCompatActivity {

    protected boolean isNameInvalid(String field, String value) {
        switch (InputValidator.validateName(value)) {
            case 0:
                Toast.makeText(this, field + " cannot be null", Toast.LENGTH_LONG).show();
                return true;
            case 1:
                return false;
            case -1:
                Toast.makeText(this, field + " cannot be empty", Toast.LENGTH_LONG).show();
                return true;
            case -2:
                Toast.makeText(this, field + " should be capitalized", Toast.LENGTH_LONG).show();
                return true;
            case -3:
                Toast.makeText(this, field + " contains invalid characters", Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(this, field + " produces unknown error", Toast.LENGTH_LONG).show();
                return true;
        }
    }

    protected boolean isPasswordInvalid(String value) {
        switch (InputValidator.validatePassword(value)) {
            case 0:
                Toast.makeText(this, "Password cannot be null", Toast.LENGTH_LONG).show();
                return true;
            case 1:
                return false;
            case -1:
                Toast.makeText(this, "Password cannot be less than " + InputValidator.MIN_PASSWORD_LENGTH + " characters long", Toast.LENGTH_LONG).show();
                return true;
            case -2:
                Toast.makeText(this, "Password does not contain at least 1 non alphanumeric character", Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(this, "Password produces unknown error", Toast.LENGTH_LONG).show();
                return true;
        }
    }

    protected boolean isPhoneNumberInvalid(String value) {
        switch (InputValidator.validatePhoneNumber(value)) {
            case 0:
                Toast.makeText(this, "Phone number cannot be null", Toast.LENGTH_LONG).show();
                return true;
            case 1:
                return false;
            case -1:
                Toast.makeText(this, "Phone number contains non numbers", Toast.LENGTH_LONG).show();
                return true;
            case -2:
                Toast.makeText(this, "Phone number is not 10 numbers long", Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(this, "Phone number produces unknown error", Toast.LENGTH_LONG).show();
                return true;
        }
    }
}
