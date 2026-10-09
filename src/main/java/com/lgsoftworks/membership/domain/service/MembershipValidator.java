package com.lgsoftworks.membership.domain.service;

import com.lgsoftworks.membership.domain.exception.CustomerAlreadyHasActiveMembershipException;
import com.lgsoftworks.membership.domain.port.out.MembershipRepositoryPort;
import com.lgsoftworks.plan.domain.enums.PlanStatus;
import com.lgsoftworks.plan.domain.exception.PlanNotActiveException;
import com.lgsoftworks.plan.domain.model.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MembershipValidator {

    private final MembershipRepositoryPort membershipRepositoryPort;

    public void validate(Plan plan, UUID customerId, UUID gymId) {
        validatePlanIsActive(plan);
        validateCustomerHasNoActiveMembership(customerId, gymId);
    }

    private void validatePlanIsActive(Plan plan) {
        if (plan.getStatus() != PlanStatus.ACTIVE) {
            throw new PlanNotActiveException();
        }
    }

    private void validateCustomerHasNoActiveMembership(UUID customerId, UUID gymId) {
        if (membershipRepositoryPort.existsActiveByCustomerIdAndGymId(customerId, gymId)) {
            throw new CustomerAlreadyHasActiveMembershipException();
        }
    }
}