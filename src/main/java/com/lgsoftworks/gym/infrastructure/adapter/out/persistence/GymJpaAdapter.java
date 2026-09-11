package com.lgsoftworks.gym.infrastructure.adapter.out.persistence;

import com.lgsoftworks.gym.domain.model.Gym;
import com.lgsoftworks.gym.domain.port.out.GymRepositoryPort;
import com.lgsoftworks.gym.infrastructure.adapter.out.persistence.entity.GymEntity;
import com.lgsoftworks.gym.infrastructure.adapter.out.persistence.mapper.GymDboMapper;
import com.lgsoftworks.gym.infrastructure.adapter.out.persistence.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GymJpaAdapter implements GymRepositoryPort {

    private final GymRepository gymRepository;
    private final GymDboMapper gymDboMapper;

    @Override
    public Gym save(Gym gym) {

        GymEntity entity = gymDboMapper.toEntity(gym);

        GymEntity savedEntity = gymRepository.save(entity);

        return gymDboMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Gym> findById(UUID id) {

        return gymRepository.findById(id)
                .map(gymDboMapper::toDomain);
    }

    @Override
    public Optional<Gym> findByDocument(String document) {

        return gymRepository.findByDocument(document)
                .map(gymDboMapper::toDomain);
    }

    @Override
    public Optional<Gym> findByEmail(String email) {

        return gymRepository.findByEmail(email)
                .map(gymDboMapper::toDomain);
    }

    @Override
    public boolean existsByDocument(String document) {
        return gymRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByEmail(String email) {
        return gymRepository.existsByEmail(email);
    }
}