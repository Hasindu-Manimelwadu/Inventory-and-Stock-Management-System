package com.example.usermanagementsystem.model;

public class StaffUser extends User {

    public StaffUser() {
    }

    public StaffUser(String userId, String username, String email, String password) {
        super(userId, username, email, password, "STAFF");
    }

    @Override
    public String getPermissionLevel() {
        return "Limited Access";
    }
}