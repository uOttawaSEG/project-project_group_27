package com.example.otams_project;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private Account adminAccount;
    private Admin admin;
    private ListView accountListView;
    private List<Account> accounts;
    private String currentView = "pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        accountListView = findViewById(R.id.accountListView);
        adminAccount = LocalDataStorage.getAccount();
        if (adminAccount.getUser().getClass() != Admin.class) {
            finish();
            return;
        }
        admin = (Admin)adminAccount.getUser();
        admin.initializeActions();

        loadPendingAccounts();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adminAccount != LocalDataStorage.getAccount()) {
            finish();
        }
    }

    private void loadPendingAccounts() {  //method to load pending accounts
        currentView = "pending";
        admin.loadPendingAccounts(new AdminCallback() {
            @Override
            public void onAccountsFetched(List<Account> fetchedAccounts) {
                accounts = fetchedAccounts;
                updateAccountList();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void loadRejectedAccounts() { //method to load rejected accounts
        currentView = "rejected";
        admin.loadRejectedAccounts(new AdminCallback() {
            @Override
            public void onAccountsFetched(List<Account> fetchedAccounts) {
                accounts = fetchedAccounts;
                updateAccountList();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void updateAccountList() { //method to update the account list , called when loading accounts or approving/rejecting accounts
        if (accounts == null || accounts.size() == 0) {
            showToast("No accounts found");
            accountListView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Account account : accounts) {
            adapter.add(account.toString());
        }
        accountListView.setAdapter(adapter);

        accountListView.setOnItemClickListener((parent, view, position, id) -> {
            showAccountDialog(accounts.get(position));
        });
    }

    private void showAccountDialog(Account account) { //dialog for clicking on accounts from ListView
        new AlertDialog.Builder(this)
                .setTitle("Status: " + account.getStatus())
                .setMessage(account.toFancyString())
                .setPositiveButton("Approve", (dialog, which) -> {
                    admin.approveAccount(currentView, account, new ApprovalCallback() {
                        @Override
                        public void onApprovalSuccess() {
                            showToast("Account approved");
                            admin.reloadAccounts(currentView, new AdminCallback() {
                                @Override
                                public void onAccountsFetched(List<Account> fetchedAccounts) {
                                    accounts = fetchedAccounts;
                                    updateAccountList();
                                }

                                @Override
                                public void onError(String message) {
                                    showToast(message);
                                }
                            });
                        }

                        @Override
                        public void onApprovalFailure(String message) {
                            showToast("Failed to approve account");
                        }
                    });
                })
                .setNegativeButton("Reject", (dialog, which) -> {
                    admin.rejectAccount(account, new ApprovalCallback() {
                        @Override
                        public void onApprovalSuccess() {
                            showToast("Account rejected");
                            admin.reloadAccounts(currentView, new AdminCallback() {
                                @Override
                                public void onAccountsFetched(List<Account> fetchedAccounts) {
                                    accounts = fetchedAccounts;
                                    updateAccountList();
                                }

                                @Override
                                public void onError(String message) {
                                    showToast(message);
                                }
                            });
                        }

                        @Override
                        public void onApprovalFailure(String message) {
                            showToast("Failed to reject account");
                        }
                    });
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onPendingClick(View view) {
        loadPendingAccounts();
    }

    public void onRejectedClick(View view) {
        loadRejectedAccounts();
    }

    public void onBackClick(View view) {
        finish();
    }
}