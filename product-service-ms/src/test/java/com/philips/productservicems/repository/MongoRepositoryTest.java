package com.philips.productservicems.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.philips.model.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class MongoRepositoryTest {

@Test
  public void testSaveProduct(@Autowired MongoTemplate mongoTemplate) {

  // given
  Product product = Product.builder()
      .id("dog")
      .name("John Wick")
      .quantity(100)
      .price(1).build();

    // when
  final Product save = mongoTemplate.save(product);
  // then
    assertEquals("dog" ,save.getId());
  }


  @Test
  public void testGetProduct(@Autowired MongoTemplate mongoTemplate) {

    // given
    Product product = Product.builder()
        .id("dog")
        .name("John Wick")
        .quantity(100)
        .price(1).build();

    // when
    Product byId = mongoTemplate.findById(product.getId(), Product.class);
    // then
    assertEquals("dog" ,byId.getId());
  }
}