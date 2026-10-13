package com.lgsoftworks.membership.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.exception.CustomerByIdNotFoundException;
import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import com.lgsoftworks.membership.application.dto.mapper.MembershipModelMapper;
import com.lgsoftworks.membership.application.dto.request.MembershipRequest;
import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.application.port.in.CreateMembershipUseCase;
import com.lgsoftworks.membership.application.port.in.GetMembershipUseCase;
import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.exception.CustomerIsNotActiveException;
import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.membership.domain.port.out.MembershipRepositoryPort;
import com.lgsoftworks.membership.domain.service.MembershipValidator;
import com.lgsoftworks.plan.domain.exception.PlanByIdNotFoundException;
import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipService implements CreateMembershipUseCase, GetMembershipUseCase {

    private final CurrentUserService currentUserService;
    private final MembershipRepositoryPort membershipRepositoryPort;
    private final PlanRepositoryPort planRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final MembershipModelMapper membershipModelMapper;
    private final MembershipValidator membershipValidator;

    @Override
    public ApiResponse<MembershipResponse> execute(MembershipRequest membershipRequest) {

        UUID gymId = currentUserService.getGymId();
        UUID customerId = membershipRequest.getCustomerId();
        UUID planId = membershipRequest.getPlanId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        if (!customer.getStatus().equals(CustomerStatus.ACTIVE)) {
            throw new CustomerIsNotActiveException();
        }

        Plan plan = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        membershipValidator.validate(plan, customerId, gymId);

        LocalDate endDate = membershipRequest.getStartDate().plusDays(plan.getDurationDays());

        Membership membership = Membership.create(
                gymId,
                customerId,
                planId,
                customer.getFullName(),
                plan.getName(),
                membershipRequest.getStartDate(),
                endDate,
                membershipRequest.getPrice(),
                membershipRequest.getPaymentMethod(),
                currentUserService.getUserId()
        );

        Membership savedMembership = membershipRepositoryPort.save(membership);

        return ApiResponse.<MembershipResponse>builder()
                .message("Afiliación registrada con éxito para " + customer.getFullName())
                .data(membershipModelMapper.toResponse(savedMembership))
                .build();
    }

    @Override
    public ApiResponse<PageResponse<MembershipResponse>> findByCustomerId(UUID customerId, int page, int size) {

        UUID gymId = currentUserService.getGymId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        Page<Membership> memberships = membershipRepositoryPort
                .findByCustomerIdAndGymId(customer.getId(), gymId, buildPageable(page, size));

        return ApiResponse.<PageResponse<MembershipResponse>>builder()
                .message("Afiliaciones del cliente " + customer.getFullName() + " obtenidas correctamente")
                .data(PageResponse.from(memberships.map(membershipModelMapper::toResponse)))
                .build();
    }

    @Override
    public ApiResponse<PageResponse<MembershipResponse>> findByStatus(
            MembershipStatus membershipStatus, int page, int size) {

        UUID gymId = currentUserService.getGymId();

        Page<Membership> memberships = membershipRepositoryPort
                .findByGymIdAndStatus(gymId, membershipStatus, buildPageable(page, size));

        return ApiResponse.<PageResponse<MembershipResponse>>builder()
                .message("Afiliaciones en estado (" + membershipStatus.getLabel() + ") obtenidas correctamente")
                .data(PageResponse.from(memberships.map(membershipModelMapper::toResponse)))
                .build();
    }

    @Override
    public ApiResponse<MembershipResponse> findById(UUID id) {

        UUID gymId = currentUserService.getGymId();

        Membership membership = membershipRepositoryPort
                .findByIdAndGymId(id, gymId)
                .orElseThrow(() -> new RuntimeException("Afiliación no encontrada"));

        return ApiResponse.<MembershipResponse>builder()
                .message("Afiliación obtenida correctamente")
                .data(membershipModelMapper.toResponse(membership))
                .build();
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}