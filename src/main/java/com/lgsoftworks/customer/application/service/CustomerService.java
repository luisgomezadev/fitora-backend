package com.lgsoftworks.customer.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.customer.application.dto.mapper.CustomerModelMapper;
import com.lgsoftworks.customer.application.dto.request.CustomerFilter;
import com.lgsoftworks.customer.application.dto.request.CustomerRequest;
import com.lgsoftworks.customer.application.dto.request.CustomerUpdateRequest;
import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.customer.application.port.in.CreateCustomerUseCase;
import com.lgsoftworks.customer.application.port.in.GetCustomerUseCase;
import com.lgsoftworks.customer.application.port.in.UpdateCustomerUseCase;
import com.lgsoftworks.customer.domain.exception.CustomerByDocumentNumberNotFoundException;
import com.lgsoftworks.customer.domain.exception.CustomerByEmailNotFoundException;
import com.lgsoftworks.customer.domain.exception.CustomerByIdNotFoundException;
import com.lgsoftworks.customer.domain.exception.CustomerByPhoneNotFoundException;
import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.model.CustomerSearchCriteria;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import com.lgsoftworks.customer.domain.service.CustomerUniquenessValidator;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements CreateCustomerUseCase,
        GetCustomerUseCase, UpdateCustomerUseCase {

    private final CurrentUserService currentUserService;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerModelMapper customerModelMapper;
    private final CustomerUniquenessValidator customerUniquenessValidator;

    @Override
    public ApiResponse<CustomerResponse> execute(CustomerRequest customerRequest) {

        String email = customerRequest.getEmail();
        String documentNumber = customerRequest.getDocumentNumber();
        String phone = customerRequest.getPhone();

        UUID gymId = currentUserService.getGymId();

        customerUniquenessValidator.validate(
                email,
                documentNumber,
                phone,
                gymId
        );

        Customer customer = Customer.create(
                gymId,
                customerRequest.getDocumentType(),
                documentNumber,
                customerRequest.getFirstName(),
                customerRequest.getLastName(),
                email,
                phone,
                customerRequest.getBirthDate()
        );

        Customer savedCustomer = customerRepositoryPort.save(customer);

        CustomerResponse customerResponse =
                customerModelMapper.toResponse(savedCustomer);

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente registrado correctamente")
                .data(customerResponse)
                .build();
    }

    @Override
    public ApiResponse<PageResponse<CustomerResponse>> findByGymId(
            CustomerFilter filter,
            int page,
            int size
    ) {
        UUID gymId = currentUserService.getGymId();

        Pageable pageable = PageRequest.of(page, size);

        CustomerSearchCriteria criteria = CustomerSearchCriteria.of(
                filter.getSearch(),
                filter.getDocumentType(),
                filter.getStatus()
        );

        Page<Customer> customers =
                customerRepositoryPort.findByGymId(gymId, criteria, pageable);

        PageResponse<CustomerResponse> pageResponse =
                PageResponse.from(customers.map(customerModelMapper::toResponse));

        return ApiResponse.<PageResponse<CustomerResponse>>builder()
                .message("Clientes obtenidos correctamente")
                .data(pageResponse)
                .build();
    }

    @Override
    public ApiResponse<CustomerResponse> findByIdAndGymId(UUID id) {
        UUID gymId = currentUserService.getGymId();

        CustomerResponse customerResponse = customerRepositoryPort
                .findByIdAndGymId(id, gymId)
                .map(customerModelMapper::toResponse)
                .orElseThrow(() -> new CustomerByIdNotFoundException(id));

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente por ID obtenido")
                .data(customerResponse)
                .build();
    }

    @Override
    public ApiResponse<CustomerResponse> findByDocumentNumberAndGymId(String documentNumber) {
        UUID gymId = currentUserService.getGymId();

        CustomerResponse customerResponse = customerRepositoryPort
                .findByDocumentNumberAndGymId(documentNumber, gymId)
                .map(customerModelMapper::toResponse)
                .orElseThrow(() -> new CustomerByDocumentNumberNotFoundException(documentNumber));

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente buscado por número de documento obtenido")
                .data(customerResponse)
                .build();
    }

    @Override
    public ApiResponse<CustomerResponse> findByEmailAndGymId(String email) {
        UUID gymId = currentUserService.getGymId();

        CustomerResponse customerResponse = customerRepositoryPort
                .findByEmailAndGymId(email, gymId)
                .map(customerModelMapper::toResponse)
                .orElseThrow(() -> new CustomerByEmailNotFoundException(email));

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente buscado por correo obtenido")
                .data(customerResponse)
                .build();
    }

    @Override
    public ApiResponse<CustomerResponse> findByPhoneAndGymId(String phone) {
        UUID gymId = currentUserService.getGymId();

        CustomerResponse customerResponse = customerRepositoryPort
                .findByPhoneAndGymId(phone, gymId)
                .map(customerModelMapper::toResponse)
                .orElseThrow(() -> new CustomerByPhoneNotFoundException(phone));

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente buscado por número de celular obtenido")
                .data(customerResponse)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> update(UUID id, CustomerUpdateRequest customerRequest) {
        UUID gymId = currentUserService.getGymId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(id, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(id));

        customer.updateInformation(
                customerRequest.getFirstName(),
                customerRequest.getLastName(),
                customerRequest.getEmail(),
                customerRequest.getPhone(),
                customerRequest.getBirthDate()
        );

        Customer updatedCustomer = customerRepositoryPort.save(customer);

        CustomerResponse customerResponse = customerModelMapper.toResponse(updatedCustomer);

        return ApiResponse.<CustomerResponse>builder()
                .message("Cliente actualizado correctamente")
                .data(customerResponse)
                .build();
    }

    @Override
    public ApiResponse<Void> deactivate(UUID customerId) {
        UUID gymId = currentUserService.getGymId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        customer.deactivate();
        customerRepositoryPort.save(customer);
        return ApiResponse.<Void>builder()
                .message("Cliente desactivado correctamente")
                .build();
    }

    @Override
    public ApiResponse<Void> activate(UUID customerId) {
        UUID gymId = currentUserService.getGymId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        customer.activate();
        customerRepositoryPort.save(customer);
        return ApiResponse.<Void>builder()
                .message("Cliente activado correctamente")
                .build();
    }

    @Override
    public ApiResponse<Void> suspend(UUID customerId) {
        UUID gymId = currentUserService.getGymId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        customer.suspend();
        customerRepositoryPort.save(customer);
        return ApiResponse.<Void>builder()
                .message("Cliente suspendido correctamente")
                .build();
    }

}