package com.example.otams_project;
import java.util.List;

public interface AdminCallback {
    void onAccountsFetched(List<Account> accounts);
    void onError(String message);

}
