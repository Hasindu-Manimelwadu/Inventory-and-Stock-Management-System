package com.example.usermanagementsystem.model;

public class AdminUser extends User {

    public AdminUser() {
    }

    public AdminUser(String userId, String username, String email, String password) {
        super(userId, username, email, password, "ADMIN");
    }

    @Override
    public String getPermissionLevel() {
        return "Full Access";
    }
}