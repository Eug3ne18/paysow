package com.paysow.service;

import com.paysow.model.User;

/**
 * OOP PILLAR: ABSTRACTION
 * Controllers depend only on this interface, not on how signup/login
 * is actually implemented (hashing algorithm, validation rules, etc).
 * That implementation detail lives in UserServiceImpl and can change
 * freely without touching any controller.
 */
public interface UserService {
    User signup(String fullName, String email, String phoneNumber, String pin);
    User login(String phoneNumber, String pin);
    User findById(Long id);
    User updateProfile(Long id, String fullName, String email);
    void changePin(Long id, String currentPin, String newPin);
}
