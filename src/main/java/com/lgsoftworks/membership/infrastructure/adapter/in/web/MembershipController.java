package com.lgsoftworks.membership.infrastructure.adapter.in.web;

import com.lgsoftworks.membership.application.dto.request.MembershipRequest;
import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.application.port.in.CreateMembershipUseCase;
import com.lgsoftworks.membership.application.port.in.GetMembershipUseCase;
import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
@Validated
@Tag(name = "Membresías", description = "Operaciones relacionadas con las membresías")
public class MembershipController {

    private final CreateMembershipUseCase createMembershipUseCase;
    private final GetMembershipUseCase getMembershipUseCase;

    @Operation(summary = "Registrar una nueva afiliación")
    @PostMapping
    public ResponseEntity<ApiResponse<MembershipResponse>> createMembership(
            @Valid @RequestBody MembershipRequest membershipRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createMembershipUseCase.execute(membershipRequest));
    }

    @Operation(summary = "Obtener una afiliación por su ID")
    @GetMapping("/{membershipId}")
    public ResponseEntity<ApiResponse<MembershipResponse>> findById(
            @PathVariable UUID membershipId
    ) {
        return ResponseEntity.ok(getMembershipUseCase.findById(membershipId));
    }

    @Operation(summary = "Listar las afiliaciones de un cliente")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<PageResponse<MembershipResponse>>> findByCustomerId(
            @PathVariable UUID customerId,
            @Parameter(description = "Número de página (empieza en 0)")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Cantidad de registros por página")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(getMembershipUseCase.findByCustomerId(customerId, page, size));
    }

    @Operation(summary = "Listar las afiliaciones por estado")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MembershipResponse>>> findByStatus(
            @Parameter(description = "Estado de la afiliación: ACTIVE, EXPIRED o CANCELLED")
            @RequestParam MembershipStatus status,
            @Parameter(description = "Número de página (empieza en 0)")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Cantidad de registros por página")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(getMembershipUseCase.findByStatus(status, page, size));
    }
}