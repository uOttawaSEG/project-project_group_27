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
    public void writeNewAccount(Register caller, Account account) {

        Query query = database.child("account").orderByChild("email").equalTo(account.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    caller.writeAccountFail();
                } else {
                    createAccountEntry(account);
                    caller.writeAccountSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to find if email and password match");
            }
        });

    }

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

                                    switch (foundAccount.getRole()) {
                                        case "student":
                                            foundAccount.setUser(child.child("user").getValue(Student.class));
                                            break;
                                        case "tutor":
                                            foundAccount.setUser(child.child("user").getValue(Tutor.class));
                                            break;
                                        case "admin":
                                            foundAccount.setUser(child.child("user").getValue(Admin.class));
                                            break;
                                        default:
                                            foundAccount.setUser(child.child("user").getValue(User.class));
                                            break;
                                    }

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


    private void createAccountEntry(Account account) {
        DatabaseReference newKey = database.child("account").push();
        newKey.setValue(account);
    }

    public void getPendingAccounts(AdminCallback callback) {
        Query query = database.child("account").orderByChild("status").equalTo("pending");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Account> accounts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Account account = child.getValue(Account.class);
                    if (account != null) {

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
        Query query = database.child("account").orderByChild("status").equalTo("rejected");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Account> accounts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Account account = child.getValue(Account.class);
                    if (account != null) {

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

    public void approveAccount(String email, ApprovalCallback callback) {
        Query query = database.child("account").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    child.getRef().child("status").setValue("approved");
                }
                callback.onApprovalSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onApprovalFailure(error.getMessage());
            }
        });
    }

    public void rejectAccount(String email, ApprovalCallback callback) {
        Query query = database.child("account").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    child.getRef().child("status").setValue("rejected");
                }
                callback.onApprovalSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onApprovalFailure(error.getMessage());
            }
        });
    }
}