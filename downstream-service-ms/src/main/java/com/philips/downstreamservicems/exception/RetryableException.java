package com.philips.downstreamservicems.exception;

public class RetryableException extends RuntimeException {

  private static final long serialVersionUID = 101L;

  public RetryableException(String message) {
    super(message);
  }
}
