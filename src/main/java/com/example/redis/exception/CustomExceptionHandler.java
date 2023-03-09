package com.example.redis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

  //Error validate for param
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
    Map<String, List<String>> result = new HashMap<>();
    result.put("errors", errors);
    return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST, result));
  }

  //Error validate for body
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidException(BindException ex, WebRequest req) {
    Map<String, String> result = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      result.put(fieldName, errorMessage);
    });
    return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST, result));
  }

  @ExceptionHandler(InternalServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handlerInternalServerException(InternalServerException ex, WebRequest req) {
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(InvalidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handlerInvalidException(InvalidException ex, WebRequest req) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handlerNotFoundException(NotFoundException ex, WebRequest req) {
    return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

}