package com.philips.productservicems.exception;


public class ResourceNotFoundException extends Exception {

  private static final long serialVersionUID = 100L;

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
