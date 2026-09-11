package com.lgsoftworks.user.domain.service;

import com.lgsoftworks.user.domain.exception.UserWithEmailExistsException;
import com.lgsoftworks.user.domain.exception.UserWithUsernameExistsException;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserUniquenessValidator {

    private final UserRepositoryPort userRepositoryPort;

    public void validate(String email, String username, UUID gymId) {
        validateExistsEmail(email, gymId);
        validateExistsUsername(username, gymId);
    }

    private void validateExistsEmail(String email, UUID gymId) {
        boolean exists = userRepositoryPort.findByEmailAndGymId(email, gymId).isPresent();
        if (exists) {
            throw new UserWithEmailExistsException(email);
        }
    }

    private void validateExistsUsername(String username, UUID gymId) {
        boolean exists = userRepositoryPort.findByUsernameAndGymId(username, gymId).isPresent();
        if (exists) {
            throw new UserWithUsernameExistsException(username);
        }
    }

}