package com.example.usermanagementsystem.service;

import com.example.usermanagementsystem.model.AdminUser;
import com.example.usermanagementsystem.model.StaffUser;
import com.example.usermanagementsystem.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final String FILE_PATH = "data/users.txt";

    public UserService() {
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            System.out.println("File creation error: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Add user error: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 5) {
                    String userId = data[0];
                    String username = data[1];
                    String email = data[2];
                    String password = data[3];
                    String role = data[4];

                    if (role.equalsIgnoreCase("ADMIN")) {
                        users.add(new AdminUser(userId, username, email, password));
                    } else {
                        users.add(new StaffUser(userId, username, email, password));
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Read user error: " + e.getMessage());
        }

        return users;
    }

    public User findUserById(String userId) {
        for (User user : getAllUsers()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public boolean login(String username, String password) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User updatedUser) {
        List<User> users = getAllUsers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                if (user.getUserId().equals(updatedUser.getUserId())) {
                    writer.write(updatedUser.toFileString());
                } else {
                    writer.write(user.toFileString());
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Update user error: " + e.getMessage());
        }
    }

    public void deleteUser(String userId) {
        List<User> users = getAllUsers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                if (!user.getUserId().equals(userId)) {
                    writer.write(user.toFileString());
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Delete user error: " + e.getMessage());
        }
    }
}