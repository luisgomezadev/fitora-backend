package com.lgsoftworks.customer.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    SUSPENDED("Suspendido");

    private final String label;
}
