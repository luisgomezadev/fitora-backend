package com.lgsoftworks.membership.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.customer.domain.exception.CustomerByIdNotFoundException;
import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import com.lgsoftworks.membership.application.dto.mapper.MembershipModelMapper;
import com.lgsoftworks.membership.application.dto.request.MembershipRequest;
import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.application.port.in.CreateMembershipUseCase;
import com.lgsoftworks.membership.application.port.in.GetMembershipUseCase;
import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.membership.domain.port.out.MembershipRepositoryPort;
import com.lgsoftworks.membership.domain.service.MembershipValidator;
import com.lgsoftworks.plan.domain.exception.PlanByIdNotFoundException;
import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Plan plan = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        membershipValidator.validate(plan, customerId, gymId);

        LocalDate endDate = membershipRequest.getStartDate().plusDays(plan.getDurationDays());

        Membership membership = Membership.create(
                gymId,
                customerId,
                planId,
                membershipRequest.getStartDate(),
                endDate,
                membershipRequest.getPrice(),
                membershipRequest.getPaymentMethod(),
                currentUserService.getUserId()
        );

        Membership savedMembership = membershipRepositoryPort.save(membership);

        MembershipResponse membershipResponse =
                membershipModelMapper.toResponse(savedMembership, customer, plan);

        return ApiResponse.<MembershipResponse>builder()
                .message("Afiliación registrada con éxito para " + customer.getFullName())
                .data(membershipResponse)
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
                .data(toPageResponse(memberships, gymId))
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
                .data(toPageResponse(memberships, gymId))
                .build();
    }

    @Override
    public ApiResponse<MembershipResponse> findById(UUID id) {

        UUID gymId = currentUserService.getGymId();

        Membership membership = membershipRepositoryPort
                .findByIdAndGymId(id, gymId)
                .orElseThrow(() -> new RuntimeException("Afiliación no encontrada"));

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(membership.getCustomerId(), gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(membership.getCustomerId()));

        Plan plan = planRepositoryPort
                .findByIdAndGymId(membership.getPlanId(), gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        MembershipResponse membershipResponse =
                membershipModelMapper.toResponse(membership, customer, plan);

        return ApiResponse.<MembershipResponse>builder()
                .message("Afiliación obtenida correctamente")
                .data(membershipResponse)
                .build();

    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private PageResponse<MembershipResponse> toPageResponse(Page<Membership> memberships, UUID gymId) {
        List<MembershipResponse> responses = toResponses(memberships.getContent(), gymId);

        return PageResponse.from(new PageImpl<>(
                responses,
                memberships.getPageable(),
                memberships.getTotalElements()
        ));
    }

    private List<MembershipResponse> toResponses(List<Membership> memberships, UUID gymId) {

        if (memberships.isEmpty()) {
            return List.of();
        }

        Set<UUID> customerIds = memberships.stream()
                .map(Membership::getCustomerId)
                .collect(Collectors.toSet());

        Set<UUID> planIds = memberships.stream()
                .map(Membership::getPlanId)
                .collect(Collectors.toSet());

        Map<UUID, Customer> customers = customerRepositoryPort
                .findAllByIdInAndGymId(customerIds, gymId)
                .stream()
                .collect(Collectors.toMap(Customer::getId, Function.identity()));

        Map<UUID, Plan> plans = planRepositoryPort
                .findAllByIdInAndGymId(planIds, gymId)
                .stream()
                .collect(Collectors.toMap(Plan::getId, Function.identity()));

        return memberships.stream()
                .map(membership -> membershipModelMapper.toResponse(
                        membership,
                        customers.get(membership.getCustomerId()),
                        plans.get(membership.getPlanId())
                ))
                .toList();
    }
}