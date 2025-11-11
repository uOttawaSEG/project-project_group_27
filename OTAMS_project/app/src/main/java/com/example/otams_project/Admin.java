package com.example.otams_project;

public class Admin extends User{

    private AdminActions actions;
    public Admin(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Admin() {
        super();
    }


    //Incomplete functionality since Admin accounts are manually added
    public static Account register(String firstName, String lastName, String email , String password, String phone) {
        Admin admin = new Admin(firstName, lastName, phone);
        Account account = new Account(email, password, "admin", admin);

        return account;
    }



    //Facade Pattern Methods
    public void initializeActions() {
        this.actions = new AdminActions();
    }
    public void loadPendingAccounts(AdminCallback callback) {
        actions.loadPendingAccounts(callback);
    }
    public void loadRejectedAccounts(AdminCallback callback) {
        actions.loadPendingAccounts(callback);
    }
    public void approveAccount(String status, Account account, ApprovalCallback callback) {
        actions.approveAccount(status, account, callback);
    }
    public void rejectAccount(Account account, ApprovalCallback callback) {
        actions.rejectAccount(account, callback);
    }
    public void reloadAccounts(String status , AdminCallback callback){
        actions.reloadAccounts(status, callback);
    }



}
