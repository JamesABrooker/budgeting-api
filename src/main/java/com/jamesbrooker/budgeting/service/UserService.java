package com.jamesbrooker.budgeting.service;

import com.jamesbrooker.budgeting.model.User;
import com.jamesbrooker.budgeting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Registers a new user
    public User register(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashedPassword);
        return userRepository.save(user);
    }

    //Attempts to login a user, checks that the email exists and that the password is correct
    public User login(String email, String rawPassword) {
        //Checks that the email exists in the repository
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email or password"));
        //Checks that the passwords match
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new RuntimeException("Invalid email or password");
        //Successfully logged in
        return user;
    }
}
