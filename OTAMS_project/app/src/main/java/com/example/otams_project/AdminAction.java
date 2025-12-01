package com.example.otams_project;
//Backend code for admin activity
public class AdminAction {
    private final FirebaseAccessor accessor;

    public AdminAction() {
        accessor = FirebaseAccessor.getInstance();
    }

    public void loadPendingAccounts(AdminCallback callback) {
        accessor.getPendingAccounts(callback);
    }

    public void loadRejectedAccounts(AdminCallback callback) {
        accessor.getRejectedAccounts(callback);
    }
//Notice approve account takes status as a parameter when reject doesnt
//This is cause we now need to know where to look in our firebase depending on the status of the account
//You can approve a pending or rejected account but can only reject a pending account
    public void approveAccount(String status, Account account, ApprovalCallback callback) {
        accessor.approveAccount(status, account.getEmail(), new ApprovalCallback() {


            @Override
            public void onApprovalSuccess() {
                account.setStatus("approved");
                Emailer.sendEmailForRegistrationStatus(account, true);
                callback.onApprovalSuccess();
            }

            @Override
            public void onApprovalFailure(String message) {
                callback.onApprovalFailure(message);
            }
        });
    }
    //Already knows where to look, must be pending to be rejected
    public void rejectAccount(Account account, ApprovalCallback callback) {
        accessor.rejectAccount(account.getEmail(), new ApprovalCallback() {


            @Override
            public void onApprovalSuccess() {
                account.setStatus("rejected");
                Emailer.sendEmailForRegistrationStatus(account, false);
                callback.onApprovalSuccess();
            }

            @Override
            public void onApprovalFailure(String message) {
                callback.onApprovalFailure(message);
            }
        });
    }
    public void reloadAccounts(String status , AdminCallback callback){ //general method to reload accounts from database
        if(status.equals("pending")){
            loadPendingAccounts(callback);
        }
        else{
            loadRejectedAccounts(callback);
        }
    }




}
