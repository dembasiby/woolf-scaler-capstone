package com.dembasiby.userservice.dto.error;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String errorCode;
    private String message;
    private String details;
    private String path;
    private LocalDateTime timestamp;
}
