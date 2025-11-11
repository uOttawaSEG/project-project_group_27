package com.example.otams_project;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TutorActivity extends AppCompatActivity {

    private ListView accountListView;

    private TutorActions tutorActions;
    private String tutorEmail;
    private String currentView = "upcoming";

    private List<Sessions> currentSessions = new ArrayList<>();
    private List<AvailabilitySlots> currentSlots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        accountListView = findViewById(R.id.accountListView);
        Account account = LocalDataStorage.getAccount();
        tutorEmail = account.getEmail();
        tutorActions = new TutorActions();
        showUpcoming();
    }

    public void onUpcomingClick(View view) {
        showUpcoming();
    }

    public void onPastClick(View view) {
        showPast();
    }

    public void onPendingClick(View view) {
        showPending();
    }

    public void onAvailabilityClick(View view) {
        showAvailability();
    }

    public void onNewClick(View view) {
        createAvailabilitySlot();
    }

    public void onBackClick(View view) {
        finish();
    }

    private void showUpcoming() {
        currentView = "upcoming";


        tutorActions.loadUpcomingSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                currentSessions = sessions;
                updateSessionListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showPast() {
        currentView = "past";


        tutorActions.loadPastSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                currentSessions = sessions;
                updateSessionListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showPending() {
        currentView = "pending";


        tutorActions.loadPendingSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                currentSessions = sessions;
                updateSessionListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showAvailability() {
        currentView = "availability";


        tutorActions.loadTutorSlots(tutorEmail, new AvailabilitySlotsCallback() {
            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlots> slots) {
                currentSlots = slots;
                updateSlotListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }



    private void updateSessionListView() {
        if (currentSessions == null || currentSessions.size() == 0) {
            showToast("No sessions found");
            accountListView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Sessions session : currentSessions) {
            String displayText = session.getDate() + " " +
                    session.getStartTime() + "-" + session.getEndTime() +
                    " - " + session.getStudentEmail() +
                    " (Status: " + session.getStatus() + ")";
            adapter.add(displayText);
        }
        accountListView.setAdapter(adapter);

        accountListView.setOnItemClickListener((parent, view, position, id) -> {
            Sessions selectedSession = currentSessions.get(position);
            showSessionDialog(selectedSession);
        });
    }

    private void updateSlotListView() {
        if (currentSlots == null || currentSlots.size() == 0) {
            showToast("No availability slots found");
            accountListView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (AvailabilitySlots slot : currentSlots) {
            String displayText = slot.getDate() + " " +
                    slot.getStartTime() + "-" + slot.getEndTime() +
                    " (Approval: " + (slot.isRequiresApproval() ? "Yes" : "No") + ")";
            adapter.add(displayText);
        }
        accountListView.setAdapter(adapter);

        accountListView.setOnItemClickListener((parent, view, position, id) -> {
            AvailabilitySlots selectedSlot = currentSlots.get(position);
            showSlotDialog(selectedSlot);
        });
    }



    private void createAvailabilitySlot() {

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final android.widget.EditText dateInput = new android.widget.EditText(this);
        dateInput.setHint("Date (ex. 2025-11-15)");
        layout.addView(dateInput);

        final android.widget.EditText startTimeInput = new android.widget.EditText(this);
        startTimeInput.setHint("Start time (ex. 10:00)");
        layout.addView(startTimeInput);

        final android.widget.EditText endTimeInput = new android.widget.EditText(this);
        endTimeInput.setHint("End time (ex. 11:30)");
        layout.addView(endTimeInput);

        final android.widget.CheckBox requiresApprovalBox = new android.widget.CheckBox(this);
        requiresApprovalBox.setText("Requires tutor request approval");
        layout.addView(requiresApprovalBox);

        final android.widget.CheckBox bookedBox = new android.widget.CheckBox(this);
        bookedBox.setText("Mark as already booked");
        layout.addView(bookedBox);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Availability Slot")
                .setView(layout)
                .setPositiveButton("Create" , null)
                .setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            String start = startTimeInput.getText().toString().trim();
            String end = endTimeInput.getText().toString().trim();
            boolean requiresApproval = requiresApprovalBox.isChecked();
            boolean booked = bookedBox.isChecked();

            if (date.isEmpty() || start.isEmpty() || end.isEmpty()) {
                showToast("Please fill in all fields");
                return;
            }
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                showToast("Date must be in format YYYY-MM-DD");
                return;
            }
            if (!start.matches("([01]\\d|2[0-3]):[0-5]\\d") || !end.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                showToast("Time must be in 24-hour format HH:mm");
                return;
            }
            if (start.compareTo(end) >= 0) {
                showToast("End time must be after start time");
                return;
            }
            tutorActions.createAvailabilitySlot(this, tutorEmail, date, start, end, requiresApproval, booked);
            showToast("Slot created!");
            showAvailability();
            dialog.dismiss();
        });


    }


    private void showSessionDialog(Sessions session) {
        tutorActions.getStudentInfo(session.getStudentEmail(), new AccountCallback() {
            @Override
            public void onAccountFetched(Account account) {
                Student student = (Student) account.getUser();
                new androidx.appcompat.app.AlertDialog.Builder(TutorActivity.this)

                        .setTitle("Session Details")
                        .setMessage("Date: " + session.getDate() + "\n" +
                                "Time: " + session.getStartTime() + "-" + session.getEndTime() + "\n" +
                                "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                                "Email: " + session.getStudentEmail() + "\n" +
                                "Phone: " + student.getPhone() + "\n" +
                                "Status: " + session.getStatus())
                        .setPositiveButton("Approve", (dialog, which) -> {
                            tutorActions.approveSession(session.getSessionID());
                            showToast("Session approved");
                            refreshCurrentView();
                        })
                        .setNegativeButton("Reject", (dialog, which) -> {
                            tutorActions.rejectSession(session.getSessionID());
                            showToast("Session rejected");
                            refreshCurrentView();
                        })
                        .setNeutralButton("Cancel Session", (dialog, which) -> {
                            tutorActions.cancelSession(session.getSessionID());
                            showToast("Session cancelled");
                            refreshCurrentView();
                        })
                        .show();
            }
            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showSlotDialog(AvailabilitySlots slot) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Availability Slot")
                .setMessage("Date: " + slot.getDate() + "\n" +
                        "Time: " + slot.getStartTime() + "-" + slot.getEndTime() + "\n" +
                        "Requires Approval: " + (slot.isRequiresApproval () ? "Yes" : "No"))
                .setPositiveButton("Delete", (dialog, which) -> {
                    tutorActions.deleteSlot(slot.getSlotID());
                    showToast("Slot deleted");
                    showAvailability();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    private void refreshCurrentView() {
        switch (currentView) {
            case "upcoming":
                showUpcoming();
                break;
            case "past":
                showPast();
                break;
            case "pending":
                showPending();
                break;
            case "availability":
                showAvailability();
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}