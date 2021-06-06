package com.philips.downstreamservicems.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DownStreamResponseExceptionHandler {

  @ExceptionHandler({RetryableException.class, ListenerExecutionFailedException.class})
  public final ResponseEntity<Object> handlePatientNotFoundExceptions(
      RetryableException ex) {
    System.out.println( ex.getMessage() + " RetryAttempt :: ");
    return null;
  }
}
