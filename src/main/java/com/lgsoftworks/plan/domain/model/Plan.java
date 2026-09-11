package com.lgsoftworks.plan.domain.model;

import com.lgsoftworks.plan.domain.enums.PlanStatus;
import com.lgsoftworks.plan.domain.exception.InvalidPlanDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Plan {

    private UUID id;
    private UUID gymId;

    private String name;
    private String description;

    private Integer durationDays;

    private BigDecimal price;

    private PlanStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Plan(
            UUID gymId,
            String name,
            String description,
            Integer durationDays,
            BigDecimal price
    ) {
        validateGymId(gymId);
        validateName(name);
        validateDuration(durationDays);
        validatePrice(price);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.name = name.trim();
        this.description = description != null
                ? description.trim()
                : null;
        this.durationDays = durationDays;
        this.price = price;
        this.status = PlanStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Plan create(
            UUID gymId,
            String name,
            String description,
            Integer durationDays,
            BigDecimal price
    ) {
        return new Plan(
                gymId,
                name,
                description,
                durationDays,
                price
        );
    }

    public static Plan reconstitute(
            UUID id,
            UUID gymId,
            String name,
            String description,
            Integer durationDays,
            BigDecimal price,
            PlanStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        Plan plan = new Plan();

        plan.id = id;
        plan.gymId = gymId;
        plan.name = name;
        plan.description = description;
        plan.durationDays = durationDays;
        plan.price = price;
        plan.status = status;
        plan.createdAt = createdAt;
        plan.updatedAt = updatedAt;

        return plan;
    }

    private void validateGymId(UUID gymId) {
        if (gymId == null) {
            throw new InvalidPlanDataException("Gym is required");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidPlanDataException("Plan name is required");
        }

        if (name.trim().length() < 3) {
            throw new InvalidPlanDataException(
                    "Plan name must have at least 3 characters"
            );
        }

        if (name.trim().length() > 100) {
            throw new InvalidPlanDataException(
                    "Plan name cannot exceed 100 characters"
            );
        }
    }

    private void validateDuration(Integer durationDays) {
        if (durationDays == null) {
            throw new InvalidPlanDataException(
                    "Plan duration is required"
            );
        }

        if (durationDays <= 0) {
            throw new InvalidPlanDataException(
                    "Plan duration must be greater than zero"
            );
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidPlanDataException("Plan price is required");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPlanDataException(
                    "Plan price cannot be negative"
            );
        }
    }

    public void update(
            String name,
            String description,
            Integer durationDays,
            BigDecimal price
    ) {
        validateName(name);
        validateDuration(durationDays);
        validatePrice(price);

        this.name = name.trim();
        this.description = description != null
                ? description.trim()
                : null;
        this.durationDays = durationDays;
        this.price = price;

        touch();
    }

    public void activate() {
        this.status = PlanStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = PlanStatus.INACTIVE;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}