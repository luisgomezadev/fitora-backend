package com.lgsoftworks.attendance.infrastructure.adapter.in.web;

import com.lgsoftworks.attendance.application.dto.request.AttendanceFilter;
import com.lgsoftworks.attendance.application.dto.request.AttendanceRequest;
import com.lgsoftworks.attendance.application.dto.response.AttendanceResponse;
import com.lgsoftworks.attendance.application.port.in.CheckOutAttendanceUseCase;
import com.lgsoftworks.attendance.application.port.in.CreateAttendanceUseCase;
import com.lgsoftworks.attendance.application.port.in.GetAttendanceUseCase;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
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
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
@Validated
public class AttendanceController {

    private final CreateAttendanceUseCase createAttendanceUseCase;
    private final CheckOutAttendanceUseCase checkOutAttendanceUseCase;
    private final GetAttendanceUseCase getAttendanceUseCase;

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @Valid @RequestBody AttendanceRequest attendanceRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createAttendanceUseCase.execute(attendanceRequest));
    }

    @PatchMapping("/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(
            @Valid @RequestBody AttendanceRequest attendanceRequest
    ) {
        return ResponseEntity.ok(checkOutAttendanceUseCase.checkOut(attendanceRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AttendanceResponse>>> getAll(
            @ModelAttribute AttendanceFilter filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(getAttendanceUseCase.findByGymId(filter, page, size));
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getById(
            @PathVariable UUID attendanceId
    ) {
        return ResponseEntity.ok(getAttendanceUseCase.findById(attendanceId));
    }
}