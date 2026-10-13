package com.lgsoftworks.customer.infrastructure.adapter.in.web;

import com.lgsoftworks.customer.application.dto.request.CustomerFilter;
import com.lgsoftworks.customer.application.dto.request.CustomerRequest;
import com.lgsoftworks.customer.application.dto.request.CustomerUpdateRequest;
import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.customer.application.port.in.CreateCustomerUseCase;
import com.lgsoftworks.customer.application.port.in.GetCustomerUseCase;
import com.lgsoftworks.customer.application.port.in.UpdateCustomerUseCase;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest customerRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createCustomerUseCase.execute(customerRequest));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateRequest customerRequest
    ) {
        return ResponseEntity.ok(updateCustomerUseCase.update(customerId, customerRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAll(
            @ParameterObject @ModelAttribute CustomerFilter filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(getCustomerUseCase.findByGymId(filter, page, size));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(
            @PathVariable UUID customerId
    ) {
        return ResponseEntity.ok(getCustomerUseCase.findByIdAndGymId(customerId));
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getByDocumentNumber(
            @PathVariable String documentNumber
    ) {
        return ResponseEntity.ok(getCustomerUseCase.findByDocumentNumberAndGymId(documentNumber));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(updateCustomerUseCase.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(updateCustomerUseCase.activate(id));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(updateCustomerUseCase.suspend(id));
    }

}
