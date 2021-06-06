package com.philips.productservicems.service;

import com.philips.model.Product;
import com.philips.productservicems.exception.BadRequestException;
import com.philips.productservicems.exception.ErrorMessages;
import com.philips.productservicems.exception.InvalidSearchCriteriaException;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.exception.ResourceRequestFailedException;
import com.philips.productservicems.repository.ProductBaseRepository;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductService implements ResourceBaseService<Product> {

  private KafkaTemplate<String, Product> kafkaTemplate;
  private final String KAFKA_TOPIC_PRODUCT = "product-operations";

  private final String ADD_PRODUCT = "ADD_PRODUCT";
  private final String DELETE_PRODUCT = "DELETE_PRODUCT";
  private final String UPDATE_PRODUCT = "UPDATE_PRODUCT";

  private ProductBaseRepository productBaseRepository;

  @Autowired
  public ProductService(KafkaTemplate<String, Product> kafkaTemplate ,
      ProductBaseRepository productBaseRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.productBaseRepository = productBaseRepository;
  }

  @Cacheable("product")
  @Override
  public Product getById(String id) throws ParseException , ResourceNotFoundException,
      BadRequestException,ResourceRequestFailedException {
    try {
      if(id == null) {
        throw new BadRequestException("Bad request : ID is not valid");
      }
      Optional<Product> byId = productBaseRepository.findById(id);

      if(byId == null || byId.get() == null ) {
        throw new ResourceNotFoundException(ErrorMessages.NO_RECORDS_FOUND.toString());
      }
      return byId.get();
    } catch (RuntimeException ex) {
      throw new ResourceRequestFailedException(ErrorMessages.INTERNAL_SERVER_ERROR.toString());
    }
  }

  @Override
  public List<Product> search() throws ParseException , ResourceNotFoundException,
      ResourceRequestFailedException, InvalidSearchCriteriaException {
    try {
      List<Product> products = productBaseRepository.findAll();

      if(products == null || products.size() ==0 ) {
        throw new ResourceNotFoundException(ErrorMessages.NO_RECORDS_FOUND.toString());
      }
      return products;
    } catch (RuntimeException ex) {
      throw new ResourceRequestFailedException(ErrorMessages.INTERNAL_SERVER_ERROR.toString());
    }
  }

  @Override
  public String add(Product product) throws ParseException,BadRequestException,
      ResourceRequestFailedException {
    try {
      if(product == null || product.getId() == null || product.getId().isEmpty()) {
        throw new BadRequestException("Product passed is incorrect");
      }
      productBaseRepository.save(product);
      sendDataToDownStreamService(ADD_PRODUCT,product);
    }
    catch (RuntimeException ex) {
      throw new ResourceRequestFailedException(ErrorMessages.INTERNAL_SERVER_ERROR.toString());
    }
    return product.getId();
  }

  private void sendDataToDownStreamService(String operation, Product product) {
    try{
      kafkaTemplate.send(KAFKA_TOPIC_PRODUCT, operation , product);
    } catch (RuntimeException ex) {
      System.out.println("Error Sending messages to kafka broker : ");
    }
  }

  @Override
  public String delete(String id) throws ParseException ,BadRequestException,
      ResourceRequestFailedException, ResourceNotFoundException {
    try {
      if(id == null || id.isEmpty()) {
        throw new BadRequestException("Product ID is incorrect");
      }
      Product toDelete = Product.builder().id(id).build();
      productBaseRepository.delete(toDelete);
      sendDataToDownStreamService(DELETE_PRODUCT,toDelete);
    }
    catch (RuntimeException ex) {
      throw new ResourceRequestFailedException(ErrorMessages.INTERNAL_SERVER_ERROR.toString());
    }
    return id;
  }

  @Override
  public Product update(String id, Product product) throws ParseException,BadRequestException,
      ResourceRequestFailedException, ResourceNotFoundException  {
    try {
      if(product == null || id.isEmpty()) {
        throw new BadRequestException("Product ID is incorrect");
      }
      if(!productBaseRepository.existsById(id)) {
        System.out.println("no data found with given ID , inserting anyways !");
      }
      product.setId(id);
      productBaseRepository.save(product);
      sendDataToDownStreamService(UPDATE_PRODUCT,product);
    }
    catch (RuntimeException ex) {
      throw new ResourceRequestFailedException(ErrorMessages.INTERNAL_SERVER_ERROR.toString());
    }
    return product;
  }
}
