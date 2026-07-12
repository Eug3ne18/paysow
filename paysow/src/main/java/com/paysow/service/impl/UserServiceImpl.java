package com.paysow.service.impl;

import com.paysow.exception.DuplicateUserException;
import com.paysow.exception.InvalidPinException;
import com.paysow.exception.UserNotFoundException;
import com.paysow.model.User;
import com.paysow.repository.UserRepository;
import com.paysow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User signup(String fullName, String email, String phoneNumber, String pin) {
        validatePin(pin);

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("An account with that email already exists.");
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateUserException("An account with that phone number already exists.");
        }

        String hashedPin = passwordEncoder.encode(pin);
        User user = new User(fullName, email, phoneNumber, hashedPin);
        return userRepository.save(user);
    }

    @Override
    public User login(String phoneNumber, String pin) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("No account found for that phone number."));

        if (!passwordEncoder.matches(pin, user.getPinHash())) {
            throw new InvalidPinException("Incorrect PIN.");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    @Transactional
    public User updateProfile(Long id, String fullName, String email) {
        User user = findById(id);

        userRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateUserException("That email is already used by another account.");
            }
        });

        user.setFullName(fullName);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePin(Long id, String currentPin, String newPin) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPin, user.getPinHash())) {
            throw new InvalidPinException("Your current PIN is incorrect.");
        }
        validatePin(newPin);
        user.setPinHash(passwordEncoder.encode(newPin));
        userRepository.save(user);
    }

    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new InvalidPinException("PIN must be exactly 4 digits.");
        }
    }
}
