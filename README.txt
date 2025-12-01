# OTAMS – Online Tutoring Appointment Management System  
SEG2105 – Introduction to Software Engineering (Fall 2025)  
Group 27

## Team Members
- Mohamad Zalzale  
- Trevor Choi  
- Carter Charlebois  
- Lucas Busoi  

## Administrator Credentials
Email: admin.root@gmail.com  
Password: admin123@

---

# Deliverable 4 – Student Features & Full Integration

Deliverable 4 completes the OTAMS application by implementing the full **Student workflow**, finalizing end-to-end integration between all three roles, and adding the required unit tests.

This deliverable includes:
- Student session searching and booking  
- Viewing upcoming and past sessions  
- Seeing approval/pending/rejected status  
- Session cancellation rules (24-hour restriction)  
- Tutor rating system  
- Search by course code with tutor name + rating  
- Prevention of overlapping bookings  
- Prevention of deleting booked tutor slots  
- At least four local unit tests  
- Final integration and UI linking all features together  

---

# How to Use the App

## 1. Installation
Install the APK from the release:  
`Project_Group_27_debug.apk`

## 2. Registration
Users may register as:
- Student  
- Tutor  

Registrations must be approved by the Administrator.

## 3. Administrator Workflow
Log in using the credentials above to:
- View pending registration requests  
- Approve or reject them  
- Re-approve previously rejected requests  

## 4. Tutor Workflow
Once approved, Tutors can:
- Create availability slots (30-minute increments only)  
- Enable or disable auto-approval  
- View upcoming, pending, and past sessions  
- View student information  
- Approve, reject, or cancel sessions  
- Delete availability slots **only if not booked** (as required in D4)

## 5. Student Workflow (Deliverable 4)
Students can now:
- View **upcoming sessions** (sorted by date)
- View **past sessions**
- See status: **approved, pending, rejected**
- Cancel:
  - pending sessions at any time  
  - approved sessions only if more than 24 hours remain
- Search for available sessions by **course code**
- See tutor **name** and **average rating**
- Book a session (slot disappears from results)
- Cannot book overlapping sessions
- Rate Tutors (1–5 stars) after completed sessions

---

# Technical Details

| Item | Description |
|------|-------------|
| Language | Java |
| IDE | Android Studio |
| Database | Firebase Realtime Database |
| Minimum SDK | 24 |
| Key Classes | StudentAction, TutorActions, FirebaseAccessor, Sessions, AvailabilitySlots |
| Key Interfaces | StudentSessionCallback, SessionsCallback, AvailabilitySlotsCallback |

---

# New Additions in Deliverable 4

### StudentAction Class (NEW)
- Fetches all student sessions  
- Splits them into **past** and **upcoming**  
- Books slots and writes sessions to Firebase  
- Prevents double/overlapping bookings  
- Designed to integrate directly with StudentActivity XML  

### StudentActivity (NEW)
- UI for seeing upcoming and past sessions  
- Buttons to cancel sessions  
- Displays session status clearly  
- Search bar for course codes  
- Displays tutor name + average rating  
- Rating dialog after completed sessions  

### FirebaseAccessor (Expanded)
- Added `getStudentSessions()`  
- Integrated with StudentSessionCallback  
- Added logic preventing tutors from deleting booked slots  

### Unit Tests (NEW)
At least four local unit tests were implemented covering:
- Date comparison and sorting logic  
- Slot-overlap detection  
- Session cancellation rules  
- Rating functionality  

---

# Files Included in Release
- APK: `Project_Group_27_debug.apk`
- UML Diagram: `Deliverable 4 UML.pdf`
- Final Report: `Deliverable 4 Report.pdf`
- README: `README_D4.md`
- Demo Video: `Deliverable 4 Demo.mp4`
- Local Unit Tests: `app/src/test/...`

---

# Submission Information
Release Tag: `v0.4`  
Release Title: `Deliverable 4`  
All required files uploaded to GitHub Release.

---
