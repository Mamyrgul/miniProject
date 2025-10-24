package com.example.miniproject.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(
      String message,
      HttpStatus httpStatus
) {
}
