package com.philips.productservicems.exception;

public class ResourceRequestFailedException extends Exception{
  private static final long serialVersionUID = 100L;

  public ResourceRequestFailedException(String message) {
    super(message);
  }
}
