package com.jamesbrooker.budgeting.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = { @UniqueConstraint(columnNames = "email") }
)

//User class holds all necessary information about the user
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //Constructor
    public User(String email, String hashedPassword) {
        this.email = email;
        this.password = hashedPassword;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    //Default Constructor required by JPA
    protected User() {}

    // Getters & setters (needed for JPA to access fields)
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }

}
