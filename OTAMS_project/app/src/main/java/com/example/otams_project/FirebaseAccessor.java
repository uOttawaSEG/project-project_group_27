package com.example.otams_project;

import android.util.Log;

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
    private static FirebaseAccessor accessor;

    private FirebaseAccessor() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAccessor getInstance() {
        if (accessor == null) {
            accessor = new FirebaseAccessor();
        }
        return accessor;
    }

    //Now checks if email exists in pending database and account database before writing new account
    public void writeNewAccount(RegisterCallback caller, Account account) {

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
//Checks if email exists in account, pending, or rejected database before logging in
    public void doesEmailMatchPassword(LoginCallback caller, String email, String password) {
        doesEmailMatchAtQueryPassword(caller, email, password, "account");
    }

    private void doesEmailMatchAtQueryPassword(LoginCallback caller, String email, String password, String queryLocation) {
        Query query = database.child(queryLocation).orderByChild("email").equalTo(email);
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
                                    //No duplicate emails in database
                                    //No need to check others
                                    caller.denySignIn();
                                }
                                return;
                            }
                    }
                } else {

                    switch (queryLocation) {
                        case "account":
                            doesEmailMatchAtQueryPassword(caller, email, password, "pending");
                            break;
                        case "pending":
                            doesEmailMatchAtQueryPassword(caller, email, password, "rejected");
                            break;
                        case "rejected":
                            caller.denySignIn();
                            break;
                        default:
                            Log.d("DatabaseSearch", "Unexpected queryLocation");
                            caller.denySignIn();
                            break;
                    }

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

    private void createPendingEntry(Account account) {
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

    //approveAccount now takes status to know where to look in firebase
    //If account is found, taken out from pending/rejected and moved to account
    public void approveAccount(String status, String email, ApprovalCallback callback) {
        Query query = database.child(status).orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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


    public void createAvailabilitySlot(AvailabilitySlots slot) {
        DatabaseReference newKey = database.child("availability_slots").push();
        slot.setSlotID(newKey.getKey());
        newKey.setValue(slot);
    }

    public void createSession(Sessions session) {
        DatabaseReference newKey = database.child("sessions").push();
        session.setSessionID(newKey.getKey());
        newKey.setValue(session);
    }


    public void bookSlot(String slotID) {
        database.child("availability_slots").child(slotID).child("booked").setValue(true);

    }

    public void deleteSlot(String slotID) {
        database.child("availability_slots").child(slotID).removeValue();
    }

    public void updateSessionStatus(String sessionID, String status) {
        database.child("sessions").child(sessionID).child("status").setValue(status);
    }


    public void getTutorSlots(String tutorEmail, AvailabilitySlotsCallback callback) {
        Query query = database.child("availability_slots").orderByChild("tutorEmail").equalTo(tutorEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AvailabilitySlots> slots = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    AvailabilitySlots slot = child.getValue(AvailabilitySlots.class);
                    if (slot != null) {
                        slots.add(slot);
                    }
                }
                callback.onAvailabilitySlotsFetched(slots);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getTutorSessions(String tutorEmail, SessionsCallback callback) {
        Query query = database.child("sessions").orderByChild("tutorEmail").equalTo(tutorEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Sessions> sessions = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Sessions session = child.getValue(Sessions.class);
                    if (session != null) {
                        session.setSessionID(child.getKey());
                        sessions.add(session);
                    }
                }
                callback.onSessionsFetched(sessions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getAccountByEmail(String email, AccountCallback callback) {
        Query query = database.child("account").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Account account = child.getValue(Account.class);
                        if (account != null) {
                            loadUserData(account, child);
                            callback.onAccountFetched(account);
                            return;
                        }
                    }
                }
                callback.onError("Account not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

}










