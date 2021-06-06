package com.philips.productservicems.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.philips.model.Product;
import com.philips.productservicems.ProductServiceMsApplication;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.service.ProductService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = {ProductServiceMsApplication.class})

public class ProductControllerTest {

  @InjectMocks
  ProductController productController;
  List<Product> productList;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @MockBean
  private ProductService productService;

  private MockMvc mockMvc;
  private Product product;

  @BeforeEach
  void setUp() throws Exception {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
  void getProductByIdSuccess() throws Exception {
    when(productService.getById("dog")).thenReturn(product);

    mockMvc
        .perform(
            get("/api/supply-chain/dog")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value("dog"));
  }
  @Test
  void deleteProductByIdSuccess() throws Exception {
    when(productService.delete("dog")).thenReturn("dog");
    MvcResult mvcResult =
    mockMvc
        .perform(
            delete("/api/supply-chain/dog")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json")).andReturn();

    assertEquals( "dog",mvcResult.getResponse().getContentAsString());

  }
  @Test
  void searchProductSuccess() throws Exception {
    when(productService.search()).thenReturn(productList);

    MvcResult mvcResult = mockMvc
        .perform(
            get("/api/supply-chain")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json")).andReturn();

  }

  @Test
  void getProductsByProductNotFound() throws Exception {

    when(productService.getById("156"))
        .thenThrow(ResourceNotFoundException.class);

    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/supply-chain/156")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);
  }

}
