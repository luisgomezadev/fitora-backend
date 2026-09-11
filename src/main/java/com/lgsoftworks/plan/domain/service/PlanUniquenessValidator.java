package com.lgsoftworks.plan.domain.service;

import com.lgsoftworks.plan.domain.exception.PlanWithNameExistsException;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlanUniquenessValidator {

    private final PlanRepositoryPort planRepositoryPort;

    public void validate(String name, UUID gymId) {
        if (planRepositoryPort.existsByNameAndGymId(name, gymId)) {
            throw new PlanWithNameExistsException();
        }
    }

}
