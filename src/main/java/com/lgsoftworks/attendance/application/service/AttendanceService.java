package com.lgsoftworks.attendance.application.service;

import com.lgsoftworks.attendance.application.dto.mapper.AttendanceModelMapper;
import com.lgsoftworks.attendance.application.dto.request.AttendanceFilter;
import com.lgsoftworks.attendance.application.dto.request.AttendanceRequest;
import com.lgsoftworks.attendance.application.dto.response.AttendanceResponse;
import com.lgsoftworks.attendance.application.port.in.CheckOutAttendanceUseCase;
import com.lgsoftworks.attendance.application.port.in.CreateAttendanceUseCase;
import com.lgsoftworks.attendance.application.port.in.GetAttendanceUseCase;
import com.lgsoftworks.attendance.domain.exception.AttendanceByIdNotFoundException;
import com.lgsoftworks.attendance.domain.exception.CustomerWithoutActiveMembershipException;
import com.lgsoftworks.attendance.domain.exception.InvalidAttendanceDataException;
import com.lgsoftworks.attendance.domain.exception.OpenAttendanceNotFoundException;
import com.lgsoftworks.attendance.domain.model.Attendance;
import com.lgsoftworks.attendance.domain.model.AttendanceSearchCriteria;
import com.lgsoftworks.attendance.domain.port.out.AttendanceRepositoryPort;
import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.exception.CustomerByIdNotFoundException;
import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import com.lgsoftworks.membership.domain.exception.CustomerIsNotActiveException;
import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.membership.domain.port.out.MembershipRepositoryPort;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceService
        implements CreateAttendanceUseCase, CheckOutAttendanceUseCase, GetAttendanceUseCase {

    private final CurrentUserService currentUserService;
    private final AttendanceRepositoryPort attendanceRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final MembershipRepositoryPort membershipRepositoryPort;
    private final AttendanceModelMapper attendanceModelMapper;

    @Override
    public ApiResponse<AttendanceResponse> execute(AttendanceRequest attendanceRequest) {

        UUID gymId = currentUserService.getGymId();
        UUID customerId = attendanceRequest.getCustomerId();

        Customer customer = customerRepositoryPort
                .findByIdAndGymId(customerId, gymId)
                .orElseThrow(() -> new CustomerByIdNotFoundException(customerId));

        if (!customer.getStatus().equals(CustomerStatus.ACTIVE)) {
            throw new CustomerIsNotActiveException();
        }

        Membership membership = membershipRepositoryPort
                .findActiveByCustomerIdAndGymId(customerId, gymId, LocalDate.now())
                .orElseThrow(CustomerWithoutActiveMembershipException::new);

        if (attendanceRepositoryPort.existsOpenByCustomerIdAndGymId(customerId, gymId)) {
            throw new InvalidAttendanceDataException(
                    "El cliente ya tiene una asistencia abierta"
            );
        }

        Attendance attendance = Attendance.checkIn(
                gymId,
                customerId,
                membership.getId(),
                customer.getFullName()
        );

        Attendance savedAttendance = attendanceRepositoryPort.save(attendance);

        return ApiResponse.<AttendanceResponse>builder()
                .message("Asistencia registrada con éxito para " + customer.getFullName())
                .data(attendanceModelMapper.toResponse(savedAttendance))
                .build();
    }

    @Override
    public ApiResponse<AttendanceResponse> checkOut(AttendanceRequest attendanceRequest) {

        UUID gymId = currentUserService.getGymId();
        UUID customerId = attendanceRequest.getCustomerId();

        Attendance attendance = attendanceRepositoryPort
                .findOpenByCustomerIdAndGymId(customerId, gymId)
                .orElseThrow(OpenAttendanceNotFoundException::new);

        attendance.checkOut();

        Attendance savedAttendance = attendanceRepositoryPort.save(attendance);

        return ApiResponse.<AttendanceResponse>builder()
                .message("Salida registrada con éxito para " + savedAttendance.getCustomerName())
                .data(attendanceModelMapper.toResponse(savedAttendance))
                .build();
    }

    @Override
    public ApiResponse<AttendanceResponse> findById(UUID id) {

        UUID gymId = currentUserService.getGymId();

        Attendance attendance = attendanceRepositoryPort
                .findByIdAndGymId(id, gymId)
                .orElseThrow(() -> new AttendanceByIdNotFoundException(id));

        return ApiResponse.<AttendanceResponse>builder()
                .message("Asistencia obtenida correctamente")
                .data(attendanceModelMapper.toResponse(attendance))
                .build();
    }

    @Override
    public ApiResponse<PageResponse<AttendanceResponse>> findByGymId(
            AttendanceFilter filter, int page, int size) {

        UUID gymId = currentUserService.getGymId();

        Page<Attendance> attendances = attendanceRepositoryPort
                .findByGymId(gymId, buildCriteria(filter), buildPageable(page, size));

        return ApiResponse.<PageResponse<AttendanceResponse>>builder()
                .message("Asistencias obtenidas correctamente")
                .data(PageResponse.from(attendances.map(attendanceModelMapper::toResponse)))
                .build();
    }

    private AttendanceSearchCriteria buildCriteria(AttendanceFilter filter) {

        LocalDate startDate = filter.getStartDate();
        LocalDate endDate = filter.getEndDate();

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidAttendanceDataException(
                    "La fecha final no puede ser anterior a la fecha inicial"
            );
        }

        String search = filter.getSearch() == null || filter.getSearch().isBlank()
                ? null
                : filter.getSearch().trim();

        return AttendanceSearchCriteria.of(search, startDate, endDate);
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "checkIn"));
    }
}