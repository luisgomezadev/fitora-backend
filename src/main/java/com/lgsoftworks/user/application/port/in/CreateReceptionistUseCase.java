package com.lgsoftworks.user.application.port.in;

import com.lgsoftworks.user.application.dto.request.CreateReceptionistRequest;
import com.lgsoftworks.user.application.dto.response.UserResponse;

public interface CreateReceptionistUseCase {
    UserResponse execute(CreateReceptionistRequest request);
}
