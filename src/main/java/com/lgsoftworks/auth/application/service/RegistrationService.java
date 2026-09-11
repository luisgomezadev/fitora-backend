package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.application.dto.request.RegisterRequest;
import com.lgsoftworks.auth.application.dto.response.RegistrationResponse;
import com.lgsoftworks.emailverification.application.port.in.SendEmailVerificationUseCase;
import com.lgsoftworks.gym.domain.exception.GymWithDocumentExistsException;
import com.lgsoftworks.gym.domain.exception.GymWithEmailExistsException;
import com.lgsoftworks.gym.domain.model.Gym;
import com.lgsoftworks.gym.domain.port.out.GymRepositoryPort;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.exception.UserWithEmailExistsException;
import com.lgsoftworks.user.domain.exception.UserWithUsernameExistsException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final GymRepositoryPort gymRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailVerificationUseCase sendEmailVerificationUseCase;

    @Transactional
    public ApiResponse<RegistrationResponse> register(RegisterRequest request) {

        // 1. Validar que el gym no exista
        if (gymRepositoryPort.existsByDocument(request.getGymDocument())) {
            throw new GymWithDocumentExistsException(
                    request.getGymDocument()
            );
        }

        if (gymRepositoryPort.existsByEmail(request.getGymEmail())) {
            throw new GymWithEmailExistsException(
                    request.getGymEmail()
            );
        }

        // 2. Validar que el usuario no exista
        if (userRepositoryPort.existsByEmail(request.getEmail())) {
            throw new UserWithEmailExistsException(
                    request.getEmail()
            );
        }

        if (userRepositoryPort.existsByUsername(request.getUsername())) {
            throw new UserWithUsernameExistsException(
                    request.getUsername()
            );
        }

        // 3. Crear Gym
        Gym gym = Gym.create(
                request.getGymName(),
                request.getGymDocument(),
                request.getGymEmail(),
                request.getGymPhone(),
                request.getGymAddress(),
                request.getGymCity()
        );

        // 4. Guardar Gym
        Gym savedGym = gymRepositoryPort.save(gym);

        // 5. Crear ADMIN
        User admin = User.create(
                savedGym.getId(),
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                UserRole.ADMIN
        );

        // 6. Guardar ADMIN
        User savedAdmin = userRepositoryPort.save(admin);

        // 7. Enviar código de verificación
        sendEmailVerificationUseCase.execute(savedAdmin.getId(), savedAdmin.getEmail());

        RegistrationResponse data = RegistrationResponse.builder()
                .gymId(savedGym.getId())
                .userId(savedAdmin.getId())
                .username(savedAdmin.getUsername())
                .email(savedAdmin.getEmail())
                .role(savedAdmin.getRole().name())
                .build();

        // 8. Construir respuesta
        return ApiResponse.<RegistrationResponse>builder()
                .message("Registro completado correctamente")
                .data(data)
                .build();
    }

}
