package com.lgsoftworks.user.application.dto.response;

import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private UserRole role;

}
