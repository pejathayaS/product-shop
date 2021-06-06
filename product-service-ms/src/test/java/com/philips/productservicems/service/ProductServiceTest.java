package com.philips.productservicems.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.philips.model.Product;
import com.philips.productservicems.exception.BadRequestException;
import com.philips.productservicems.exception.ErrorMessages;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.exception.ResourceRequestFailedException;
import com.philips.productservicems.repository.ProductBaseRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductServiceTest {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductBaseRepository productBaseRepository;

  private Product product;
  private List<Product> productList;

  @BeforeEach
  void setU() {
    product = Product.builder()
        .id("dog")
        .name("John Wick")
        .quantity(100)
        .price(1).build();

    Product product2 = Product.builder()
        .id("antidog")
        .name("Nobody")
        .quantity(200)
        .price(2).build();

    productList = Arrays.asList(product, product2);
  }

  @Test
  public void successCallForProductById() throws Exception {
    when(productBaseRepository.findById("dog")).thenReturn(java.util.Optional.ofNullable(product));

    Product product = productService.getById("dog");
    assertEquals("dog", product.getId());
    assertEquals("John Wick", product.getName());
    assertEquals(100, product.getQuantity());
    assertEquals(1, product.getPrice());
  }

  @Test
  public void FailureCallForProductById() throws Exception {

    assertThrows(ResourceNotFoundException.class, () -> {
      when(productBaseRepository.findById("dg"))
          .thenReturn(null);
      Product p = productService.getById("dg");
    });
  }

  @Test
  public void successCallForProductSave() throws Exception {
    when(productBaseRepository.save(product)).thenReturn(product);

    String savedProduct = productService.add(product);

    assertEquals("dog", savedProduct);

  }

  @Test
  public void FailureCallForProductSaveBadInput() throws Exception {

    assertThrows(BadRequestException.class, () -> {
      when(productBaseRepository.save(product))
          .thenReturn(null);
      String p = productService.add(null);
    });
  }

  @Test
  public void successCallForProductUpdate() throws Exception {
    when(productBaseRepository.save(product)).thenReturn(product);

    Product updatedProduct = productService.update(product.getId(), product);

    assertEquals("dog", updatedProduct.getId());
    assertEquals("John Wick", updatedProduct.getName());
    assertEquals(100, updatedProduct.getQuantity());
    assertEquals(1, updatedProduct.getPrice());

  }

  @Test
  public void FailureCallForProductUpdateResource() throws Exception {

    assertThrows(BadRequestException.class, () -> {
      Product p = productService.update(null, null);
    });
  }
}
