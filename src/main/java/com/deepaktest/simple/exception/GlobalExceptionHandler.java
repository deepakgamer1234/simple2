package com.deepaktest.simple.exception;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ExceptionInfo> handleUserNotFoundException(UserNotFoundException ex) {
    logger.info("coming to the exception");
    ExceptionInfo info = new ExceptionInfo();
    info.setCode("not getting user");
    info.setMsg(ex.getMessage());
    info.setDate(LocalDateTime.now());

    return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + ex.getMessage());
  }
  // @ResponseStatus(HttpStatus.BAD_REQUEST)
  // @ExceptionHandler(MethodArgumentNotValidException.class)
  // public Map<String, String>
  // handleValidationExceptions(MethodArgumentNotValidException ex) {
  // Map<String, String> errors = new HashMap<>();
  // ex.getBindingResult().getFieldErrors().forEach(error -> {
  // errors.put(error.getField(), error.getDefaultMessage());
  // });
  // return errors;
  // }

  // @ExceptionHandler(Exception.class)
  // public ResponseEntity<ExceptionInfo> handleGlobalException(Exception ex) {
  // logger.error("General error occurred: {}", ex.getMessage());
  // ExceptionInfo info = new ExceptionInfo();
  // info.setCode("GENERAL_ERROR");
  // info.setMsg("An internal server error occurred. Please try again.");
  // info.setDate(LocalDateTime.now());

  // return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
  // }

}
