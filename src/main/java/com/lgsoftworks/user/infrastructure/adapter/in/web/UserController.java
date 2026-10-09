package com.lgsoftworks.user.infrastructure.adapter.in.web;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.user.application.dto.request.CreateReceptionistRequest;
import com.lgsoftworks.user.application.dto.response.UserResponse;
import com.lgsoftworks.user.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createReceptionist(@Valid @RequestBody CreateReceptionistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.execute(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findById() {
        return ResponseEntity.ok(userService.findByGymId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findByIdAndGymId(id));
    }

}
