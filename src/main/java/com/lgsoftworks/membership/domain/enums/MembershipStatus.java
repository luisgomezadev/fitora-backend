package com.lgsoftworks.membership.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipStatus {
    ACTIVE("Activa"),
    EXPIRED("Vencida"),
    CANCELLED("Cancelada");

    private final String label;

}