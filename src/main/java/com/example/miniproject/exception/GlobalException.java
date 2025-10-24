package com.example.miniproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = AlreadyExist.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleExist(AlreadyExist exception) {
        return ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleBadCredentials(BadCredentialsException exception) {
        return ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAccessDenied(ForbiddenException exception) {
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(value = FileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleFileException(FileException exception) {
        return ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFound(NotFoundException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequest(BadRequestException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
    @ExceptionHandler(value = PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handlePermissionDenied(PermissionDeniedException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse accessDenied(AccessDeniedException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN).
                message(e.getMessage())
                .build();
    }
    @ExceptionHandler(InvalidDateRangeException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ExceptionResponse accessDenied(InvalidDateRangeException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.ALREADY_REPORTED).
                message(e.getMessage())
                .build();
    }
}
