package com.example.otams_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;


public class FirebaseAccessor {

    private final DatabaseReference database;

    public FirebaseAccessor() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @SuppressWarnings("unused")
    //Now checks if email exists in pending database and account database before writing new account
    public void writeNewAccount(Register caller, Account account) {

        Query query = database.child("pending").orderByChild("email").equalTo(account.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    caller.writeAccountFail();
                } else {
                    Query accountQuery = database.child("account").orderByChild("email").equalTo(account.getEmail());
                    accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                caller.writeAccountFail();
                            } else {

                                createPendingEntry(account);
                                caller.writeAccountSuccess();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Failed to find if email email exists");
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to find if email and password match");
            }
        });

    }
//Checks if email exists in account database before logging in
    public void doesEmailMatchPassword(Login caller, String email, String password) {
        Query query = database.child("account").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Account foundAccount = child.getValue(Account.class);
                        if (foundAccount != null)
                            if (foundAccount.getPassword() != null) {
                                if (password.equals(foundAccount.getPassword())) {
                                    loadUserData(foundAccount, child);
                                    caller.approveSignIn(foundAccount);
                                } else {
                                    caller.denySignIn();
                                }
                                return;
                            }
                    }
                } else {
                    caller.denySignIn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to find if email and password match");
            }
        });

    }

//Methods for writing to firebase, depending on status of account
    private void createAccountEntry(Account account) {
        DatabaseReference newKey = database.child("account").push();
        newKey.setValue(account);
    }

    private void createPendingEntry(Account account){
        DatabaseReference newKey = database.child("pending").push();
        newKey.setValue(account);
    }

    private void createRejectedEntry(Account account) {
        DatabaseReference newKey = database.child("rejected").push();
        newKey.setValue(account);
    }

    public void getPendingAccounts(AdminCallback callback) {
        Query query = database.child("pending");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Account> accounts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Account account = child.getValue(Account.class);
                    if (account != null) {
                        loadUserData(account, child);
                        accounts.add(account);
                    }
                }
                callback.onAccountsFetched(accounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Failed to fetch accounts");
            }
        });
    }

    public void getRejectedAccounts(AdminCallback callback) {
        Query query = database.child("rejected");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Account> accounts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Account account = child.getValue(Account.class);
                    if (account != null) {
                        loadUserData(account, child);
                        accounts.add(account);
                    }
                }
                callback.onAccountsFetched(accounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Failed to fetch accounts");
            }
        });
    }

    public void approveAccount(String status, String email, ApprovalCallback callback) {
        Query query = database.child(status).orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {

                        Account account = child.getValue(Account.class);
                        if (account != null) {
                            loadUserData(account, child);
                            account.setStatus("approved");
                            createAccountEntry(account);
                            child.getRef().removeValue();
                            callback.onApprovalSuccess();


                        }
                        else{
                            callback.onApprovalFailure("Account not found");
                        }
                    }

                }
                else{
                    callback.onApprovalFailure("Account not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onApprovalFailure(error.getMessage());
            }
        });
    }

    public void rejectAccount(String email, ApprovalCallback callback) {
        Query query = database.child("pending").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Account account = child.getValue(Account.class);
                        if (account != null) {
                            loadUserData(account, child);
                            account.setStatus("rejected");
                            createRejectedEntry(account);
                            child.getRef().removeValue();
                            callback.onApprovalSuccess();


                        }
                        else{
                            callback.onApprovalFailure("Account not found");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onApprovalFailure(error.getMessage());
            }
        });
    }

    private void loadUserData(Account account , DataSnapshot child) {
        switch (account.getRole()) {
            case "student":
                account.setUser(child.child("user").getValue(Student.class));
                break;
            case "tutor":
                account.setUser(child.child("user").getValue(Tutor.class));
                break;
            case "admin":
                account.setUser(child.child("user").getValue(Admin.class));
                break;
            default:
                account.setUser(child.child("user").getValue(User.class));
                break;
        }
    }

}