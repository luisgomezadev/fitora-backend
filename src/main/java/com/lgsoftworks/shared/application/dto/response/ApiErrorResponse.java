package com.lgsoftworks.shared.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private Integer code;
    private String message;
    private List<String> details;
    private LocalDateTime timestamp;

}