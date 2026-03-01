package com.jamesbrooker.budgeting.service;

import com.jamesbrooker.budgeting.exception.EmailAlreadyExistsException;
import com.jamesbrooker.budgeting.model.User;
import com.jamesbrooker.budgeting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

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
            throw new EmailAlreadyExistsException(email);
        }
        if(!validPassword(rawPassword)) {throw new RuntimeException("Invalid password");}
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashedPassword);
        return userRepository.save(user);
    }

    //Attempts to login a user
    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new BadCredentialsException("Invalid credentials");
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email"));
    }
}
