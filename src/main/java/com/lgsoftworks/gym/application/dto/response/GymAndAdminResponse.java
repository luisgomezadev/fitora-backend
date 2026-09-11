package com.lgsoftworks.gym.application.dto.response;

import com.lgsoftworks.user.application.dto.response.UserResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GymAndAdminResponse {
    private UserResponse userResponse;
    private GymResponse gymResponse;
}
