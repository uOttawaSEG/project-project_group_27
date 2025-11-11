package com.example.otams_project;

public class LocalDataStorage {

    private static Account account = new Account();
    private static boolean loginStatus = false;

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        if (account == null)
            return;
        LocalDataStorage.account = account;
        LocalDataStorage.loginStatus = account.getEmail() != null;
    }

    public static boolean isLoginStatus() {
        return loginStatus;
    }
}
