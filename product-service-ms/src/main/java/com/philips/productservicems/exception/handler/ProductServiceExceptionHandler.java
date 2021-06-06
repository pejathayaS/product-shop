package com.philips.productservicems.exception.handler;

import com.philips.productservicems.exception.BadRequestException;
import com.philips.productservicems.exception.InvalidSearchCriteriaException;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.exception.ResourceRequestFailedException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProductServiceExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ResourceNotFoundException.class})
  public final ResponseEntity<ErrorResponse> handleProductNotFoundExceptions(
      ResourceNotFoundException ex, WebRequest request) {
    return createExceptionResponse(
        new Date(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.OK);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<ErrorResponse> handleInvalidExceptions(BadRequestException ex, WebRequest request) {
    return createExceptionResponse(new Date(), ex.getLocalizedMessage(),
        request.getDescription(false), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ResourceRequestFailedException.class})
  public final ResponseEntity<ErrorResponse> handleProductRequestFailedExceptions(
      ResourceRequestFailedException ex, WebRequest request) {
    return createExceptionResponse(
        new Date(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({InvalidSearchCriteriaException.class})
  public final ResponseEntity<ErrorResponse> handleInvalidSearchCriteriaFailedExceptions(
      InvalidSearchCriteriaException ex, WebRequest request) {
    return createExceptionResponse(
        new Date(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> createExceptionResponse(Date date, String errorMessage,
      String details, HttpStatus statusCode) {
    ErrorResponse exceptionResponse = ErrorResponse.builder()
        .timestamp(date)
        .error(errorMessage)
        .details(details)
        .build();
    return new ResponseEntity<>(exceptionResponse, statusCode);
  }

}
