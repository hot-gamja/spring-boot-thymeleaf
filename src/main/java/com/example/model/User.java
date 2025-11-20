package com.example.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * User domain model
 */
@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public User() {
    }

    // Constructor with parameters
    public User(String username, String email, String fullName, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

}