package com.example.otams_project;

public class LocalDataStorage {

    private static Account account = new Account();
    private static boolean loginStatus = false;

    public static Account getAccount() {
        if (account == null)
            return new Account();
        return account;
    }

    public static void setAccount(Account account) {
        if (account == null) {
            LocalDataStorage.account = null;
            LocalDataStorage.loginStatus = false;
            return;
        }

        User user = account.getUser();
        user.setAccount(account);
        LocalDataStorage.account = new Account(account.getEmail(), account.getPassword(), account.getRole(), user);
        LocalDataStorage.account.setStatus(account.getStatus());
        LocalDataStorage.loginStatus = true;
    }

    public static boolean isLoginStatus() {
        return loginStatus;
    }
}
