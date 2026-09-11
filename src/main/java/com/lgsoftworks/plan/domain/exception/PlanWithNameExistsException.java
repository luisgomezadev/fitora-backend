package com.lgsoftworks.plan.domain.exception;

public class PlanWithNameExistsException extends RuntimeException {
    public PlanWithNameExistsException() {
        super("Ya existe un plan con ese nombre registrado en el gym");
    }
}
