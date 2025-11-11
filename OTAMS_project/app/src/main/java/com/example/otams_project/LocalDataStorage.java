package com.example.otams_project;

public class LocalDataStorage {

    private Account account;
    private boolean loginStatus;
    private static LocalDataStorage storage;

    private LocalDataStorage() {
        account = new Account();
        loginStatus = false;
    }

    public static LocalDataStorage getInstance() {
        if (storage == null) {
            storage = new LocalDataStorage();
        }
        return storage;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        if (account == null)
            return;
        this.account = account;
        this.loginStatus = account.getEmail() != null;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }
}
