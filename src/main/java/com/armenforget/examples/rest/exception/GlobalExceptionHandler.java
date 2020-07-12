package com.armenforget.examples.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    return getErrorResponseEntity(ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
    return getErrorResponseEntity(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<?> getErrorResponseEntity(Exception ex, WebRequest request, HttpStatus httpStatus) {
    ErrorResponse errorResponse = new ErrorResponse(
            new Date(),
            httpStatus.toString(),
            ex.getMessage(),
            request.getDescription(false)
    );
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

}
