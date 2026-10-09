package com.lgsoftworks.membership.application.port.in;

import com.lgsoftworks.membership.application.dto.request.MembershipRequest;
import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

public interface CreateMembershipUseCase {
    ApiResponse<MembershipResponse> execute(MembershipRequest membershipRequest);
}
