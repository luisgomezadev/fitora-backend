package com.lgsoftworks.user.domain.model;

import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.enums.UserStatus;
import com.lgsoftworks.user.domain.exception.InvalidUserDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class User {

    private UUID id;
    private UUID gymId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(
            UUID gymId,
            String username,
            String email,
            String password,
            UserRole role
    ) {
        validateGymId(gymId);
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.username = username.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
        this.status = UserStatus.ACTIVE;

        this.emailVerified = false;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static User create(
            UUID gymId,
            String username,
            String email,
            String password,
            UserRole role
    ) {
        return new User(
                gymId,
                username,
                email,
                password,
                role
        );
    }

    public static User reconstitute(
            UUID id,
            UUID gymId,
            String username,
            String email,
            String password,
            UserRole role,
            UserStatus status,
            boolean emailVerified,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        User user = new User();

        user.id = id;
        user.gymId = gymId;
        user.username = username;
        user.email = email;
        user.password = password;
        user.role = role;
        user.status = status;
        user.emailVerified = emailVerified;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;

        return user;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        touch();
    }

    private void validateGymId(UUID gymId) {
        if (gymId == null) {
            throw new InvalidUserDataException("Gym is required");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidUserDataException("Username is required");
        }

        if (username.trim().length() < 4) {
            throw new InvalidUserDataException(
                    "Username must have at least 4 characters"
            );
        }

        if (username.trim().length() > 50) {
            throw new InvalidUserDataException(
                    "Username cannot exceed 50 characters"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidUserDataException("Email is required");
        }

        String emailRegex =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.trim().matches(emailRegex)) {
            throw new InvalidUserDataException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidUserDataException("Password is required");
        }

        if (password.length() < 8) {
            throw new InvalidUserDataException(
                    "Password must have at least 8 characters"
            );
        }
    }

    private void validateRole(UserRole role) {
        if (role == null) {
            throw new InvalidUserDataException("User role is required");
        }
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email.trim().toLowerCase();

        // Si cambia el correo, debe volver a verificarlo
        this.emailVerified = false;

        touch();
    }

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username.trim();
        touch();
    }

    public void changePassword(String password) {
        validatePassword(password);
        this.password = password;
        touch();
    }

    public void changeRole(UserRole role) {
        validateRole(role);
        this.role = role;
        touch();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}