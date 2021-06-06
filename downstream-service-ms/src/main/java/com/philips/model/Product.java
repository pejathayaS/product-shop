package com.philips.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

public class Product {
  private String id;
  private String name;
  private Number price;
  private Integer quantity;
}
