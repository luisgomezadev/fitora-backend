package com.lgsoftworks.membership.application.port.in;

import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;

import java.util.UUID;

public interface GetMembershipUseCase {

    ApiResponse<PageResponse<MembershipResponse>> findByCustomerId(UUID customerId, int page, int size);

    ApiResponse<PageResponse<MembershipResponse>> findByStatus(MembershipStatus membershipStatus, int page, int size);

    ApiResponse<MembershipResponse> findById(UUID id);

}
