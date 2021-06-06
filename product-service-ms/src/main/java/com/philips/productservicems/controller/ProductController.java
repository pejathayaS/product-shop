package com.philips.productservicems.controller;

import com.philips.model.Product;
import com.philips.productservicems.exception.BadRequestException;
import com.philips.productservicems.exception.InvalidSearchCriteriaException;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.exception.ResourceRequestFailedException;
import com.philips.productservicems.service.ResourceBaseService;
import io.swagger.annotations.Api;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Product")
@RequestMapping("/api/supply-chain")
public class ProductController {

  @Autowired
  @Qualifier("productService")
  ResourceBaseService<Product> productManagementService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProduct(@PathVariable(value = "id") String id) throws ParseException, ResourceNotFoundException,
      BadRequestException,ResourceRequestFailedException {
    Product productDto = productManagementService.getById(id);
    return new ResponseEntity<>(productDto, HttpStatus.OK);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Product>> search() throws ParseException , ResourceNotFoundException,
      ResourceRequestFailedException, InvalidSearchCriteriaException {
    List<Product> allProducts = productManagementService.search();
    return new ResponseEntity<>(allProducts, HttpStatus.OK);
  }

  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
  public String addProduct(@RequestBody Product product) throws ParseException,BadRequestException,
      ResourceRequestFailedException {
    return productManagementService.add(product);
  }

  @DeleteMapping("{id}")
  public String deleteProduct(@PathVariable(value = "id") String id) throws ParseException ,BadRequestException,
      ResourceRequestFailedException, ResourceNotFoundException {
    return productManagementService.delete(id);
  }

  @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") String id, @RequestBody Product product)
      throws ParseException,BadRequestException , ResourceRequestFailedException, ResourceNotFoundException {
      return new ResponseEntity<>(productManagementService.update(id,product),HttpStatus.OK);
  }
}
