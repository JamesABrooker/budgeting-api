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
        //Checks that the email doesn't already exist
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        //Checks that the password is valid
        if(!validPassword(rawPassword)) {throw new RuntimeException("Invalid password");}

        //Hashes password, creates a new user with the email and hashed password, and adds it to the repository
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

    //Updates password for user
    public User updatePassword(User user, String newRawPassword){
        if (validPassword(newRawPassword)) {
            user.setPassword(passwordEncoder.encode(newRawPassword));
            return userRepository.save(user);
        }
        return user;
    }

    //Function to check if a given password is a valid password
    private Boolean validPassword(String rawPassword) {
        return rawPassword != null && !rawPassword.isBlank();
    }
}
