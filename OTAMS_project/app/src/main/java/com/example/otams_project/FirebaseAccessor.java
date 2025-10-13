package com.example.otams_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import kotlin.Suppress;

public class FirebaseAccessor {

    private final DatabaseReference database;

    public FirebaseAccessor() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewAccount(MainActivity caller, Account account) {

        Query query = database.child("account").orderByChild("email").equalTo(account.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        caller.writeAccountFail();
                        return;
                    }
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

    public void doesEmailMatchPassword(MainActivity caller, String email, String password) {
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

}