package com.reddit.redditcloneback.exception;

import com.reddit.redditcloneback.common.response.ErrorResponse;
import io.lettuce.core.RedisException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(
            BusinessException businessException,
            HttpServletRequest request
    ) {
        return ErrorResponse.toResponseError(businessException.getExceptions(), request);
    }

    @ExceptionHandler(value = RedisException.class)
    public ResponseEntity<ErrorResponse> redisException(
            HttpServletRequest request
    ) {
        return ErrorResponse.toResponseError(Exceptions.NO_CONNECTED, request);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointException(
            HttpServletRequest request
    ) {
        return ErrorResponse.toResponseError(Exceptions.NO_DATA,request);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialException(
            HttpServletRequest request
    ) {
        return ErrorResponse.toResponseError(Exceptions.AUTH_FAIL, request);
    }
}
