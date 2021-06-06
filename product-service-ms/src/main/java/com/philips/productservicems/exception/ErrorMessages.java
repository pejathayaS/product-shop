package com.philips.productservicems.exception;

import lombok.Getter;

@Getter
public enum ErrorMessages {
  NO_RECORDS_FOUND("No product found for provided id"),
  RECORD_ALREADY_EXISTS("Record already exists"),
  INTERNAL_SERVER_ERROR("Boom!!. Please repeat this operation later.");

  private String errorMessage;
  ErrorMessages(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }
}
