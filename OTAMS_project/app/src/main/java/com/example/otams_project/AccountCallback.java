package com.example.otams_project;

public interface AccountCallback {

    public void onAccountFetched(Account account);

    public void onError(String message);
}
