package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Service layer
 */
@Service
@Transactional
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Create new user
     */
    public User createUser(User user) {
        // Business logic validation can be added here
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        userDao.insert(user);
        return user;
    }

    /**
     * Update existing user
     */
    public User updateUser(User user) {
        // Check if user exists
        User existingUser = userDao.findById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + user.getId());
        }

        userDao.update(user);
        return userDao.findById(user.getId());
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(Long id) {
        User existingUser = userDao.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userDao.delete(id);
    }

    /**
     * Get total user count
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userDao.count();
    }
}