package com.example.otams_project;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements RatingCallback{
    private ListView listView;

    private StudentAction studentAction;

    private Account studentAccount;

    private String currentView = "unbookedSlots";

    private List<Session> mySessions = new ArrayList<>();

    private List<AvailabilitySlot> availableSlots = new ArrayList<>();

    private EditText searchInput;

    private boolean past;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        listView = findViewById(R.id.listView);
        past = false;
        studentAccount = LocalDataStorage.getAccount();
        studentAction = new StudentAction();
        searchInput = findViewById(R.id.courseSearch);
    }

    public void onAvailabilitySlotsClick(View view) {
        searchInput.setText("", TextView.BufferType.EDITABLE);
        showUnbookedSlots();
    }

    public void onUpcomingSessionsClick(View view) {
        searchInput.setText("", TextView.BufferType.EDITABLE);
        showUpcomingSessions();
    }

    public void onPastSessionsClick(View view) {
        searchInput.setText("", TextView.BufferType.EDITABLE);
        showPastSessions();
    }

    public void onClearSearchClick(View view) {
        searchInput.setText("", TextView.BufferType.EDITABLE);
        refreshCurrentView();
    }
    public void onBackClick(View view) {
        finish();
    }

    private void showUnbookedSlots() {
        currentView = "unbookedSlots";

        past = false;
        studentAction.loadAvailableSlots(new AvailabilitySlotsCallback() {

            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlot> availabilitySlots) {
                availableSlots = availabilitySlots;
                updateAvailableSlots();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showUpcomingSessions() {
        currentView = "upcoming";

        past = false;
        studentAction.loadMySessions(studentAccount.getEmail(), true, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Session> sessions) {
                mySessions = sessions;
                updateSessionListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showPastSessions() {
        currentView = "past";

        past = true;
        studentAction.loadMySessions(studentAccount.getEmail(), false, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Session> sessions) {
                mySessions = sessions;
                updateSessionListView();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void updateAvailableSlots() {
        if (availableSlots == null || availableSlots.isEmpty()) {
            showToast("No slots found");
            listView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        String searchQuery = searchInput.getText().toString().strip().toLowerCase();

        for (AvailabilitySlot slot : availableSlots) {

            if (slot.getCourses() != null) {
                if (slot.getCourses().strip().toLowerCase().contains(searchQuery) /*&& studentAction.isNumHoursAhead(slot.getDate(), slot.getStartTime(), 0)*/) {
                    String displayText = slot.getDate() + " " +
                            slot.getStartTime() + "-" + slot.getEndTime() +
                            " - " + slot.getTutorEmail() +
                            " (Courses: " + slot.getCourses() + ")";
                    adapter.add(displayText);
                }
            }

        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AvailabilitySlot selectedSlot = availableSlots.get(position);
            showSlotDialog(selectedSlot);
        });
    }

    private void updateSessionListView() {
        if (mySessions == null || mySessions.isEmpty()) {
            showToast("No sessions found");
            listView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        String searchQuery = searchInput.getText().toString().strip().toLowerCase();

        for (Session session : mySessions) {

            if (session.getCourses() != null) {
                if (session.getCourses().strip().toLowerCase().contains(searchQuery)) {
                    String displayText = session.getDate() + " " +
                            session.getStartTime() + "-" + session.getEndTime() +
                            " (Approval Status: " + session.getStatus() + ")";
                    adapter.add(displayText);
                }
            }

        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Session selectedSession = mySessions.get(position);
            showSessionDialog(selectedSession);
        });
    }

    private void showSessionDialog(Session session) {
        studentAction.getTutorInfo(session.getTutorEmail(), new AccountCallback() {
            @Override
            public void onAccountFetched(Account account) {
                Tutor tutor = (Tutor) account.getUser();
                AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(StudentActivity.this)
                        .setTitle("Session Details")
                        .setMessage("Date: " + session.getDate() + "\n" +
                                "Time: " + session.getStartTime() + "-" + session.getEndTime() + "\n" +
                                "Tutor: " + tutor.getFirstName() + " " + tutor.getLastName() + "\n" +
                                "Tutor Rating: " + tutor.getRating() + "\n" +
                                "Courses: " + session.getCourses() + "\n" +
                                "Tutor Email: " + session.getTutorEmail() + "\n" +
                                "Phone: " + tutor.getPhone() + "\n" +
                                "Status: " + session.getStatus());

                if (past && !session.isTutorRated()) {
                    EditText ratingInput = new EditText(StudentActivity.this);
                    ratingInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    ratingInput.setHint("Rating from 1-5");
                    builder.setView(ratingInput)
                            .setPositiveButton("Rate", (dialog, which) -> {
                                try {
                                    if (Integer.parseInt(ratingInput.getText().toString()) <= 5 && Integer.parseInt(ratingInput.getText().toString()) >= 1) {
                                        tutor.addRating(Integer.parseInt(ratingInput.getText().toString()), account.getEmail(), StudentActivity.this, session);
                                    } else {
                                        Toast.makeText(StudentActivity.this, "Rating must be an integer from 1-5", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NumberFormatException e) {
                                    Log.d("rating", "String received is not parseable to integer");
                                    Toast.makeText(StudentActivity.this, "Rating must be an integer from 1-5", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (!past && "approved".equals(session.getStatus())){
                    builder.setPositiveButton("Cancel", (dialog, which) -> {

                        if (TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 24)) {
                            studentAction.cancelSession(session.getSessionID(), session.getSlotID());
                            showToast("Session canceled");
                            refreshCurrentView();
                        } else {
                            showToast("Session cannot be canceled within 24 hours of start");
                        }
                    });
                } else if (!past && "pending".equals(session.getStatus())) {
                    builder.setPositiveButton("Cancel", (dialog, which) -> {
                        studentAction.cancelSession(session.getSessionID(), session.getSlotID());
                        showToast("Session canceled");
                        refreshCurrentView();
                    });

                }

                builder.setNegativeButton("Back", null)
                        .show();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void showSlotDialog(AvailabilitySlot slot) {
        studentAction.getTutorInfo(slot.getTutorEmail(), new AccountCallback() {
            @Override
            public void onAccountFetched(Account account) {
                Tutor tutor = (Tutor) account.getUser();
                new androidx.appcompat.app.AlertDialog.Builder(StudentActivity.this)
                        .setTitle("Availability Slot")
                        .setMessage("Date: " + slot.getDate() + "\n" +
                                "Time: " + slot.getStartTime() + "-" + slot.getEndTime() + "\n" +
                                "Requires Approval: " + (slot.isRequiresApproval () ? "Yes" : "No") + "\n" +
                                "Tutor Email: " + slot.getTutorEmail() + "\n" +
                                "Tutor: " + tutor.getFirstName() + " " + tutor.getLastName() + "\n" +
                                "Tutor Rating: " + tutor.getRating() + "\n" +
                                "Courses: " + slot.getCourses())
                        .setPositiveButton("Book", (dialog, which) -> {
                            studentAction.bookSlot(StudentActivity.this, slot, studentAccount.getEmail());
                            refreshCurrentView();
                        })
                        .setNegativeButton("Back", null)
                        .show();
            }
            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void refreshCurrentView() {
        switch (currentView) {
            case "unbookedSlots":
                showUnbookedSlots();
                break;
            case "past":
                showPastSessions();
                break;
            case "upcoming":
                showUpcomingSessions();
                break;
        }
    }

    public void onSearchClick(View view) {
        refreshCurrentView();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ratingChangeSuccess(Session session) {
        session.rateTutor();
        showToast("Successfully added rating");
    }

    @Override
    public void ratingChangeFail() {
        showToast("Failed to add rating");
    }
}