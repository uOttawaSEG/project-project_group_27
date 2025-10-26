package com.example.otams_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class FirebaseAccessor {

    private final DatabaseReference database;

    public FirebaseAccessor() {
        database = FirebaseDatabase.getInstance().getReference();
    }

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

    public void writeNewRegistrationRequest(RegistrationRequest request) {
        DatabaseReference newKey = database.child("request").push();
        newKey.setValue(request);
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

    public void readAllPendingRequests(ListRequest caller, String status) {
        Query query = database.child("request").orderByChild("status").equalTo(status);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LinkedList<RegistrationRequest> listOfRequests = new LinkedList<RegistrationRequest>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {

                        listOfRequests.addLast((RegistrationRequest) child.getValue());

                    }
                }
                caller.retrieveList(listOfRequests);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed read " + status + " requests");
            }
        });
    }




    private void createAccountEntry(Account account) {
        DatabaseReference newKey = database.child("account").push();
        newKey.setValue(account);
    }




}