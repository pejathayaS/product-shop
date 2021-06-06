package com.philips.downstreamservicems.listener;

import com.philips.downstreamservicems.exception.RetryableException;
import com.philips.downstreamservicems.service.CommunicationService;
import com.philips.model.Product;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ProductDataConsumer {

  private CommunicationService communicationService;

  @Autowired
  public ProductDataConsumer(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @KafkaListener(topics = "product-operations", groupId = "supply-chain-1",containerFactory = "productTopicListenerContainerFactory")
  public void consumeProductData(@Payload ConsumerRecord<String,Product> recievedPackage) throws RetryableException {
    System.out.println("Message Consumed" + recievedPackage);
    communicationService.processData(recievedPackage.key(), recievedPackage.value());
  }

  @KafkaListener(topics = "product-retry-1-topic", groupId = "supply-chain-1",containerFactory = "retryTopicListenerContainerFactory")
  public void consumeRetryQueue(ConsumerRecord<String,Product> recievedPackage) throws Exception {
    System.out.println("Message Consumed from Retry Q" + recievedPackage);
    communicationService.processData(recievedPackage.key(),recievedPackage.value());
  }
}
