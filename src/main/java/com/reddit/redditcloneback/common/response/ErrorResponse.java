package com.reddit.redditcloneback.common.response;

import com.reddit.redditcloneback.exception.Exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String httpStatus;
    private String httpMethod;
    private String message;
    private String path;
    private Long timestamp;

    public ErrorResponse(
            Exceptions exceptions,
            HttpServletRequest request
    ) {
        this.code = exceptions.getCode();
        this.httpStatus = exceptions.getHttpStatus().toString();
        this.httpMethod = request.getMethod();
        this.message = exceptions.getMessage();
        this.path = request.getRequestURI();
        this.timestamp = Instant.now().getEpochSecond();
    }

    public static ResponseEntity<ErrorResponse> toResponseError(
            Exceptions exceptions,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(exceptions, request);

        return ResponseEntity
                .status(exceptions.getHttpStatus())
                .body(errorResponse);
    }
}
