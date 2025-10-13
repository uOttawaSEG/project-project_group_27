package com.example.otams_project;

public class InputValidator {

    public static final int MIN_PASSWORD_LENGTH = 8;
    private static final int SPACE = 32;
    private static final int a = 97;
    private static final int z = 122;
    private static final int A = 65;
    private static final int Z = 90;
    private static final int DASH = 45;
    private static final int ZERO = 48;
    private static final int NINE = 57;
    private static final int ATSIGN = 64;
    private static final int PERIOD = 46;


    //Returns 0 if name is null
    //Returns 1 if name is valid
    //Returns -1 if name is empty
    //Returns -2 if name is not capitalized
    //Returns -3 if name has invalid characters
    //Assumes input string name is already stripped

    public static int validateName(String name) {
        if (name == null) {
            return 0;
        }

        if (name.isEmpty()) {
            return -1;
        }

        char[] nameArray = name.toCharArray();
        char[] nameArrayLower = name.toLowerCase().toCharArray();

        if (nameArray[0] == nameArrayLower[0]) {
            return -2;
        }

        for (char c : nameArray) {
            if (!((a <= c && c <= z) || (A <= c && c <= Z) || (c == SPACE) || (c == DASH))) {
                return -3;
            }
        }

        return 1;
    }


    //Returns 0 if password is null
    //Returns 1 if password is valid
    //Returns -1 if password too short
    //Returns -2 if password contains no non alphanumeric characters
    public static int validatePassword(String password) {
        if (password == null) {
            return 0;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return -1;
        }

        char[] passwordArray = password.toCharArray();

        for (char c : passwordArray) {
            if (!((a <= c && c <= z) || (A <= c && c <= Z) || (ZERO <= c && c <= NINE))) {
                return 1;
            }
        }

        return -2;
    }

    //Returns 0 if number is null
    //Returns 1 if number is valid
    //Returns -1 if number contains non numeric characters
    //Returns -2 if number is too short
    public static int validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return 0;
        }

        char[] phoneNumberArray = phoneNumber.toCharArray();

        for (char c : phoneNumberArray) {
            if (!(ZERO <= c && c <= NINE)) {
                return -1;
            }
        }

        if (phoneNumber.length() != 10) {
            return -2;
        }

        return 1;
    }

    //Returns 0 if email is null
    //Returns 1 if email is valid
    //Returns -1 if email is missing @ or .
    //Returns -2 if email has no username (first part)
    //Returns -3 if email has no mail server
    //Returns -4 if email has no domain
    public static int validateEmailAddress(String emailAddress) {

        if (emailAddress == null) {
            return 0;
        }

        char[] emailAddressArray = emailAddress.toCharArray();

        boolean hasAtSign = false;
        boolean hasPeriod = false;
        for (char c : emailAddressArray) {
            if (c == ATSIGN) {
                if (hasAtSign) {
                    return -5;
                }
                hasAtSign = true;
            }
            if (hasAtSign && c == PERIOD) {
                hasPeriod = true;
            }
        }

        if (!hasPeriod) {
            return -1;
        }

        String[] usernameSplitFromEmail = emailAddress.split("@");
        if (usernameSplitFromEmail[0].isEmpty()) {
            return -2;
        }

        String[] mailServerAndDomain = usernameSplitFromEmail[1].split("\\.",2);

        if (mailServerAndDomain[0].isEmpty()) {
            return -3;
        } else if (mailServerAndDomain[1].isEmpty()) {
            return -4;
        }

        return 1;
    }

    //Returns 0 if input is null
    //Returns 1 if input exists
    //Returns -1 if input is empty
    public static int validateExistence(String input) {

        if (input == null) {
            return 0;
        }

        if (input.isEmpty()) {
            return -1;
        }

        return 1;
    }
}
