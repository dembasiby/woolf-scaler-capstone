package com.dembasiby.productservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error message", example = "Invalid price range")
    private String message;

    @Schema(description = "Error code", example = "INVALID_PRICE_RANGE")
    private String errorCode;

    @Schema(description = "Timestamp of the error")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Request path", example = "/api/v1/search/products")
    private String path;

    @Schema(description = "Detailed error description", example = "Minimum price cannot be greater than maximum price")
    private String details;
}