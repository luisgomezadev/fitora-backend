package com.lgsoftworks.plan.domain.exception;

public class PlanByIdNotFoundException extends RuntimeException {
    public PlanByIdNotFoundException() {
        super("Plan con ID no encontrado");
    }
}
