package com.lgsoftworks.user.domain.port.out;

import com.lgsoftworks.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findByIdAndGymId(UUID id, UUID gymId);

    Optional<User> findByUsernameAndGymId(String username, UUID gymId);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID userId);

    Optional<User> findByEmailAndGymId(String email, UUID gymId);

    List<User> findByGymId(UUID gymId);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}