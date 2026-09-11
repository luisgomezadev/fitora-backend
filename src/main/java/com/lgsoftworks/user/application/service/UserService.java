package com.lgsoftworks.user.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.user.application.dto.mapper.UserModelMapper;
import com.lgsoftworks.user.application.dto.request.CreateReceptionistRequest;
import com.lgsoftworks.user.application.dto.response.UserResponse;
import com.lgsoftworks.user.application.port.in.CreateReceptionistUseCase;
import com.lgsoftworks.user.application.port.in.GetUserUseCase;
import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.exception.UserByEmailNotFoundException;
import com.lgsoftworks.user.domain.exception.UserByIdNotFoundException;
import com.lgsoftworks.user.domain.exception.UserByUsernameNotFoundException;
import com.lgsoftworks.user.domain.exception.UserCreationNotAllowedException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import com.lgsoftworks.user.domain.service.UserUniquenessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements GetUserUseCase, CreateReceptionistUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserModelMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUniquenessValidator uniquenessValidator;
    private final CurrentUserService currentUserService;

    @Override
    public UserResponse execute(CreateReceptionistRequest request) {

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        UUID gymId = currentUserService.getGymId();
        UserRole userRole = currentUserService.getRole();

        if(userRole != UserRole.ADMIN) throw new UserCreationNotAllowedException();

        uniquenessValidator.validate(email, username, gymId);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.create(
                gymId,
                username,
                email,
                encodedPassword,
                UserRole.RECEPTIONIST
        );

        User savedUser = userRepositoryPort.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public ApiResponse<UserResponse> findByIdAndGymId(UUID id) {
        UUID gymId = currentUserService.getGymId();

        UserResponse userResponse = userRepositoryPort
                .findByIdAndGymId(id, gymId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserByIdNotFoundException(id));

        return ApiResponse.<UserResponse>builder()
                .message("Información del usuario obtenida")
                .data(userResponse)
                .build();
    }

    @Override
    public ApiResponse<UserResponse> findByEmailAndGymId(String email) {
        UUID gymId = currentUserService.getGymId();

        UserResponse userResponse = userRepositoryPort
                .findByEmailAndGymId(email.trim().toLowerCase(), gymId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserByEmailNotFoundException(email));

        return ApiResponse.<UserResponse>builder()
                .message("Información del usuario obtenida")
                .data(userResponse)
                .build();
    }

    @Override
    public ApiResponse<UserResponse> findByUsernameAndGymId(String username) {
        UUID gymId = currentUserService.getGymId();

        UserResponse userResponse = userRepositoryPort
                .findByUsernameAndGymId(username.trim(), gymId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserByUsernameNotFoundException(username));

        return ApiResponse.<UserResponse>builder()
                .message("Información del usuario obtenida")
                .data(userResponse)
                .build();
    }

    @Override
    public ApiResponse<List<UserResponse>> findByGymId() {
        UUID gymId = currentUserService.getGymId();

        List<UserResponse> userResponses = userRepositoryPort
                .findByGymId(gymId)
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .message("Información de tus usuarios obtenida correctamente")
                .data(userResponses)
                .build();
    }

}