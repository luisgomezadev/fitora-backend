package com.lgsoftworks.customer.domain.model;

import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.enums.DocumentType;

public record CustomerSearchCriteria(
        String search,
        DocumentType documentType,
        CustomerStatus status
) {
    public static CustomerSearchCriteria of(
            String search,
            DocumentType documentType,
            CustomerStatus status
    ) {
        return new CustomerSearchCriteria(search, documentType, status);
    }
}