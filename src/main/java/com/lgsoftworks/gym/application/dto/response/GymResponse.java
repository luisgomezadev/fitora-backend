package com.lgsoftworks.gym.application.dto.response;

import com.lgsoftworks.gym.domain.enums.GymStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GymResponse {

    private UUID id;
    private String name;
    private String document;
    private String email;
    private String phone;
    private String address;
    private String city;
    private GymStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}