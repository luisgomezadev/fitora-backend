package com.lgsoftworks.user.infrastructure.adapter.out.persistence;

import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.mapper.UserDboMapper;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final UserDboMapper userDboMapper;

    @Override
    public Optional<User> findByIdAndGymId(UUID id, UUID gymId) {
        Optional<UserEntity> optionalUser = userRepository.findByIdAndGymId(id, gymId);
        return optionalUser.map(userDboMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameAndGymId(String username, UUID gymId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameAndGymId(username, gymId);
        return optionalUser.map(userDboMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(userDboMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.map(userDboMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailAndGymId(String email, UUID gymId) {
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndGymId(email, gymId);
        return optionalUser.map(userDboMapper::toDomain);
    }

    @Override
    public List<User> findByGymId(UUID gymId) {
        return userRepository.findByGymIdAndRole(gymId, UserRole.RECEPTIONIST)
                .stream().map(userDboMapper::toDomain).toList();
    }

    @Override
    public User save(User user) {
        UserEntity savedUser = userRepository.save(userDboMapper.toEntity(user));
        return userDboMapper.toDomain(savedUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
