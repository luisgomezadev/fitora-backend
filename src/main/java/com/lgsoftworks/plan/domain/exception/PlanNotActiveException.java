package com.lgsoftworks.plan.domain.exception;

public class PlanNotActiveException extends RuntimeException {
    public PlanNotActiveException() {
        super("El plan no está activo");
    }
}