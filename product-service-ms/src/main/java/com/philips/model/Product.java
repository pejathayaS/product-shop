package com.philips.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document("Product")
public class Product {
  @Id
  private String id;
  private String name;
  private Number price;
  private Integer quantity;
}
