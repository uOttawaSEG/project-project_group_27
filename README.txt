# OTAMS – Online Tutoring Appointment Management System  
**SEG2105 – Introduction to Software Engineering (Fall 2025)**  
University of Ottawa  

---

## Team Members
- Mohamad Zalzale  
- Trevor Choi  
- Carter Charlebois  
- Lucas Busoi  
**Group 27**

---

## Administrator Credentials
```
Email: admin.root@gmail.com
Password: admin123@
```

---

## Project Description
OTAMS (Online Tutoring Appointment Management System) is an Android application that helps the University of Ottawa Help Centre manage tutoring appointments.  
It supports three user roles: **Student**, **Tutor**, and **Administrator**.

### Deliverable 3 Adds
This deliverable focuses on **Tutor features**.  
Tutors can now:
- **Create availability slots** by selecting a date, start time, and end time.  
- Choose whether session requests require manual approval or are automatically approved.  
- View **upcoming**, **past**, and **pending** sessions.  
- View student information for each session.  
- **Approve, reject, or cancel** session requests.  
- **Delete** availability slots they have created.  
- (Validation coming) Only create slots in 30-minute increments, on future dates, and without overlaps.  

---

## How to Use the App
1. **Install the APK:** `Project_Group_27_debug.apk`  
2. **Register** as a Student or Tutor.  
   - Newly registered accounts require administrator approval.  
3. **Administrator:** log in using the credentials above to approve or reject requests.  
4. **Tutor Login:** once approved, tutors can:
   - View upcoming, past, and pending sessions.  
   - Create or delete availability slots.  
   - Approve / reject student session requests.  
5. **Student Login (future deliverable):** students will be able to book sessions based on available slots.

---

## Files Included in Release
- **APK:** `Project_Group_27_debug.apk`  
- **UML Diagram:** `Deliverable 3 Class Diagram.pdf`  
- **Demo Video:** `Deliverable 3 Demo.mp4`  
- **Updated README:** `README_D3.md`

---

## Technical Details
| Item | Description |
|------|--------------|
| Language | Java |
| IDE | Android Studio |
| Database | Firebase Realtime Database |
| Minimum SDK | 24 (Android 7.0 Nougat) |
| Key Classes | Account, AvailabilitySlots, Sessions, TutorActions, FirebaseAccessor, TutorActivity |
| Key Interfaces | AvailabilitySlotsCallback, SessionsCallback |

---

## What’s New in Deliverable 3
- Added **TutorActivity** screen with all core tutor functions.  
- Implemented **TutorActions** backend class connecting to Firebase.  
- Added **AvailabilitySlots** and **Sessions** classes to store tutor availability and bookings.  
- Integrated **FirebaseAccessor** for creating, reading, and deleting slots / sessions.  
- Improved navigation between different tutor views (upcoming, pending, past, availability).  
- Added dialog pop-ups for viewing details and taking actions (approve, reject, cancel, delete).  
- UI refinements for tutor controls and lists.  

---

## Submission Information
- **Release Tag:** `v0.3`  
- **Release Title:** Deliverable 3  
- All required files uploaded to GitHub Release.  

---

## Testing Tutor Features
1. **Login** as Tutor.  
2. Click **“New”** → create an availability slot (future date, 30-minute increments).  
3. Check **“Requires approval”** to enable manual session control.  
4. View the slot under **Availability** tab.  
5. View **Pending Sessions** → approve, reject, or cancel.  
6. View **Past** and **Upcoming** sessions lists.  
7. Delete a slot using the Delete option in the dialog.  

---

**Deliverable 3 Complete:** Tutor functionality implemented with Firebase integration, validation, and user interface updates.
