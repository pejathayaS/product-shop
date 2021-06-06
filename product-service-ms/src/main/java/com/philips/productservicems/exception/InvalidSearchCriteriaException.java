package com.philips.productservicems.exception;

public class InvalidSearchCriteriaException extends Exception{

  private static final long serialVersionUID = 2L;

  public InvalidSearchCriteriaException(String message){
    super(message);
  }
}
