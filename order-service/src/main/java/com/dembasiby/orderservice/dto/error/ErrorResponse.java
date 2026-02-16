package com.dembasiby.orderservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error message", example = "Order not found")
    private String message;

    @Schema(description = "Error code", example = "NOT_FOUND")
    private String errorCode;

    @Schema(description = "Timestamp of the error")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Request path", example = "/orders/1")
    private String path;

    @Schema(description = "Detailed error description")
    private String details;
}
