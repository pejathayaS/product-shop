package com.philips.productservicems.exception;

public class BadRequestException extends Exception {
  private static final long serialVersionUID = 100L;

  public BadRequestException(String message) {
    super(message);
  }
}
