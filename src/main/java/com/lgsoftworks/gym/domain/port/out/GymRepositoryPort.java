package com.lgsoftworks.gym.domain.port.out;

import com.lgsoftworks.gym.domain.model.Gym;

import java.util.Optional;
import java.util.UUID;

public interface GymRepositoryPort {

    Gym save(Gym gym);

    Optional<Gym> findById(UUID id);

    Optional<Gym> findByDocument(String document);

    Optional<Gym> findByEmail(String email);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);
}