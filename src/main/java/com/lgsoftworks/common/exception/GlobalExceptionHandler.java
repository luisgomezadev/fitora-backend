package com.lgsoftworks.common.exception;

import com.lgsoftworks.attendance.domain.exception.InvalidAttendanceDataException;
import com.lgsoftworks.auth.domain.exception.AccessDeniedException;
import com.lgsoftworks.auth.domain.exception.InvalidCredentialsException;
import com.lgsoftworks.auth.domain.exception.PasswordNotNullException;
import com.lgsoftworks.emailverification.domain.exception.*;
import com.lgsoftworks.gym.domain.exception.GymWithDocumentExistsException;
import com.lgsoftworks.gym.domain.exception.GymWithEmailExistsException;
import com.lgsoftworks.shared.application.dto.response.ApiErrorResponse;
import com.lgsoftworks.gym.domain.exception.InvalidGymDataException;
import com.lgsoftworks.membership.domain.exception.InvalidMembershipDataException;
import com.lgsoftworks.plan.domain.exception.InvalidPlanDataException;
import com.lgsoftworks.shared.domain.exception.ForbiddenException;
import com.lgsoftworks.student.domain.exception.*;
import com.lgsoftworks.user.domain.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- 400 BAD REQUEST: Excepciones de negocio ---
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            PasswordNotNullException.class,
            SQLIntegrityConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            DataIntegrityViolationException.class,
            InvalidUserDataException.class,
            InvalidAttendanceDataException.class,
            InvalidGymDataException.class,
            InvalidMembershipDataException.class,
            InvalidPlanDataException.class,
            InvalidStudentDataException.class,
            VerificationCodeExpired.class,
            InvalidVerificationCode.class
    })
    public ApiErrorResponse handleBadRequest(RuntimeException ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // --- 404 NOT FOUND ---
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserByEmailNotFoundException.class,
            UserByIdNotFoundException.class,
            UserByUsernameNotFoundException.class,
            VerificationCodeNotFound.class,
            StudentByPhoneNotFoundException.class,
            StudentByIdNotFoundException.class,
            StudentByEmailNotFoundException.class,
            StudentByDocumentNumberNotFoundException.class
    })
    public ApiErrorResponse handleNotFoundException(RuntimeException ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // --- 409 CONFLICT ---
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
            EmailAlreadyVerifiedException.class,
            UserWithEmailExistsException.class,
            UserWithUsernameExistsException.class,
            GymWithDocumentExistsException.class,
            GymWithEmailExistsException.class,
            StudentWithDocumentNumberExistsException.class,
            StudentWithEmailExistsException.class,
            StudentWithPhoneExistsException.class
    })
    public ApiErrorResponse handleConflict(RuntimeException ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler()
    public ApiErrorResponse handleNotFound(NoResourceFoundException ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Recurso no encontrado")
                .timestamp(LocalDateTime.now())
                .build();
    }

    // --- 401 UNAUTHORIZED ---
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            InvalidCredentialsException.class,
            AuthorizationDeniedException.class
    })
    public ApiErrorResponse handleUnauthorized(RuntimeException ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // --- 403 FORBIDDEN ---
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            AccessDeniedException.class,
            ForbiddenException.class,
            EmailNotVerifiedException.class,
            UserCreationNotAllowedException.class
    })
    public ApiErrorResponse handleAccessDenied(Exception ex) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("Acceso denegado: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // --- 400 BAD REQUEST: Errores de validación ---
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return ApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Parámetros inválidos")
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        return ApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Violación de restricción")
                .details(violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
    }
}