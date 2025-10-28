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
OTAMS (Online Tutoring Appointment Management System) is an Android app that helps the University of Ottawa Help Centre manage tutoring appointments.  
It supports three roles: **Student**, **Tutor**, and **Administrator**.

### Deliverable 2 Adds
- Full **Administrator approval workflow** using Firebase Realtime Database  
- Accounts stored with a `status` field (`pending / approved / rejected`)  
- Students and Tutors must be approved before they can log in  
- **AdminActivity**: view pending or rejected requests and approve/reject them  
- **PendingApprovalActivity**: shows message that approval is waiting  
- Rejection screen shows contact message for administration  
- Updated **Account**, **Student**, and **Tutor** classes with role-specific data  
- Clean `toString()` display of all user information (email, role, status, name, phone, program or courses)  

---

## How to Use the App
1. **Install the APK:** `Project_Group_27_debug.apk`  
2. Choose **Register** → fill the **Student** or **Tutor** form and submit.  
   - A success message appears: *“Registration successful – awaiting administrator approval.”*  
3. Return to **Login** and enter your credentials.  
   - If **approved**, you reach the Welcome screen.  
   - If **pending**, you see the “Pending Approval” screen.  
   - If **rejected**, you see a rejection message with a (fake) contact number.  
4. **Administrator Login:** use the credentials above to access AdminActivity.  
   - View pending and rejected accounts.  
   - Tap an account → popup appears → Approve ✅ or Reject ❌.  
   - Firebase updates the status immediately and the list refreshes.  

---

## Files Included in Release
- APK file (`Project_Group_27_debug.apk`)  
- UML Class Diagram (`Deliverable 2 Class Diagram.pdf`)  
- Demo video (`Deliverable 2 Demo.mp4`)  
- Updated README file (`README_D2.md`)  

---

## Technical Details
| Item | Description |
|------|--------------|
| Programming Language | Java |
| IDE | Android Studio |
| Database | Firebase Realtime Database |
| Minimum SDK | 24 (Android 7.0 Nougat) |
| Key Classes | Account, User, Student, Tutor, FirebaseAccessor, AdminActivity, PendingApprovalActivity |
| Key Interfaces | AdminCallback, ApprovalCallback |

---

## What’s New in Deliverable 2
- Integrated Firebase Realtime Database for persistent storage  
- Added Administrator dashboard (AdminActivity)  
- Added PendingApprovalActivity for unapproved users  
- Added status handling (`pending / approved / rejected`) in Account.java  
- Enhanced Account.toString() to show program/courses information  
- Implemented approval and rejection logic through FirebaseAccessor  
- Improved UI and spacing for account information display   

---

## Submission Information
- **Release Tag:** `v0.2`  
- **Release Title:** Deliverable 2  
- All required files uploaded to GitHub Release  

---

## Testing the Approval Flow
1. Register a **Student** or **Tutor** with new credentials.  
2. Try logging in — you’ll be redirected to the *Pending Approval* screen.  
3. Log in using **Admin credentials** (above).  
4. Approve or reject the account in the Admin panel.  
5. Log back in as the Student/Tutor:  
   - If approved → access the logged-in screen.  
   - If rejected → redirected to rejection message.

---

**Deliverable 2 Complete:** Functional admin approval system, Firebase integration, improved UI, and role-specific data handling.
