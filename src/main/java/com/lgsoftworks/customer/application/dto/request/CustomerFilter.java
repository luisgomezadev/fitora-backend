package com.lgsoftworks.customer.application.dto.request;

import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFilter {

    private String search;

    private DocumentType documentType;

    private CustomerStatus status;
}
