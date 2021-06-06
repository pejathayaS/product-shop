package com.philips.productservicems.repository;

import com.philips.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductBaseRepository extends MongoRepository<Product,String> {

}
