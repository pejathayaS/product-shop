package com.philips.productservicems.exception.handler;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorResponse {
  private Date timestamp;
  private String error;
  private String details;
}


