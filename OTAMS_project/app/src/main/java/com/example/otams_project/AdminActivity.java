package com.example.otams_project;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;


public class AdminActivity extends AppCompatActivity implements AdminCallback  {

    private Account adminAccount;
    private ListView accountListView;
    private FirebaseAccessor accessor;
    private List<Account> accounts;

    private String viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        accountListView = findViewById(R.id.accountListView);
        accessor = new FirebaseAccessor();
        viewer = "pending";
        accessor.getPendingAccounts(this);
        adminAccount = LocalDataStorage.getAccount();
    }

    public void onResume() {
        super.onResume();
        if (adminAccount != LocalDataStorage.getAccount())
            finish();
    }

    public void onPendingClick(View view) {
        viewer = "pending";
        accessor.getPendingAccounts(this);
    }

    public void onRejectedClick(View view) {
        viewer = "rejected";
        accessor.getRejectedAccounts(this);
    }

    public void onBackClick(View view) {
        finish();
    }

    public void onAccountsFetched(List<Account> accounts) {
        this.accounts = accounts;
        if(accounts.size() == 0){
            Toast.makeText(this, "No accounts found", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Account account : accounts) {
            adapter.add(account.toString());
        }
        accountListView.setAdapter(adapter);

        accountListView.setOnItemClickListener((parent, view, position, id) -> {
            Account selectedAccount = accounts.get(position);
            showApproveRejectDialog(selectedAccount);
        });
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showApproveRejectDialog(Account account) {

        new AlertDialog.Builder(this)
                .setTitle("status : " + account.getStatus())
                .setMessage(account.toFancyString())
                .setPositiveButton("Approve", (dialog, which) -> {
                    accessor.approveAccount(account.getEmail(), new ApprovalCallback() {
                        @Override
                        public void onApprovalSuccess() {
                            Toast.makeText(AdminActivity.this, "Account approved", Toast.LENGTH_SHORT).show();

                            Account newStatusAccount = account;
                            newStatusAccount.setStatus("approved");

                            Emailer.sendEmailForRegistrationStatus(account, true);
                            LocalNotifier.sendRequestStatusNotification(AdminActivity.this, account, true);
                            if (viewer.equals("pending")) {
                                accessor.getPendingAccounts(AdminActivity.this);
                            } else {
                                accessor.getRejectedAccounts(AdminActivity.this);
                            }
                        }
                        @Override
                        public void onApprovalFailure(String message) {
                            Toast.makeText(AdminActivity.this, "Failed to approve account", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Reject", (dialog, which) -> {
                    accessor.rejectAccount(account.getEmail(), new ApprovalCallback() {
                        @Override
                        public void onApprovalSuccess() {
                            Toast.makeText(AdminActivity.this, "Account rejected", Toast.LENGTH_SHORT).show();

                            Account newStatusAccount = account;
                            newStatusAccount.setStatus("rejected");

                            Emailer.sendEmailForRegistrationStatus(newStatusAccount, false);
                            LocalNotifier.sendRequestStatusNotification(AdminActivity.this, newStatusAccount, false);
                            if (viewer.equals("pending")) {
                                accessor.getPendingAccounts(AdminActivity.this);
                            } else {
                                accessor.getRejectedAccounts(AdminActivity.this);
                            }
                        }
                        @Override
                        public void onApprovalFailure(String message) {
                            Toast.makeText(AdminActivity.this, "Failed to reject account", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNeutralButton("Cancel" , null)
                .show();
                }














}