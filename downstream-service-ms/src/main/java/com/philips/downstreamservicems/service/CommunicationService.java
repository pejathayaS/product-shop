package com.philips.downstreamservicems.service;

import com.philips.downstreamservicems.exception.RetryableException;
import com.philips.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CommunicationService {

  private final String ADD_PRODUCT = "ADD_PRODUCT";
  private final String DELETE_PRODUCT = "DELETE_PRODUCT";
  private final String UPDATE_PRODUCT = "UPDATE_PRODUCT";

  private final String supplyChainUrl = "https://ev5uwiczj6.execute-api.eu-central-1.amazonaws.com/test/supply-chain";
  private final String deleteTrackerUrl = "https://ev5uwiczj6.execute-api.eu-central-1.amazonaws.com/test/deleteTracker";

  @Autowired
  RestTemplate restTemplate;

  public void processData(String key, Product product) {
    if (key.equalsIgnoreCase(ADD_PRODUCT) || key.equalsIgnoreCase(UPDATE_PRODUCT)) {
      communicateToSupplyChain(product);
    }
    if (key.equalsIgnoreCase(DELETE_PRODUCT)) {
        communicateToDeleteTracker(product);
    }
  }

  private void communicateToDeleteTracker(Product product) throws RetryableException{
    HttpEntity<Product> request = new HttpEntity<>(product);
    try {
      restTemplate.delete(supplyChainUrl, request, Product.class);
    } catch (RestClientException ex) {
      throw new RetryableException("Error while sending data to delete tracker");
    }
  }

  private void communicateToSupplyChain(Product product) throws RetryableException{
    HttpEntity<Product> request = new HttpEntity<>(product);
    try {
      restTemplate.postForObject(supplyChainUrl, request, Product.class);
    } catch (RestClientException ex) {
      throw new RetryableException("Error while sending data to supply chain" + product +ex.getMessage());
    }
  }
}
