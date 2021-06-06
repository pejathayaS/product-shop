package com.philips.downstreamservicems.listener.TopicListenerContainer;

import com.philips.downstreamservicems.exception.RetryableException;
import com.philips.model.Product;
import java.security.Policy;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryTopicListenerContainer {

  public static final String DLQ = "dead-letter-q";

  public ConcurrentKafkaListenerContainerFactory<String, Product> factory;

  public RetryTopicListenerContainer(ConsumerFactory consumerFactory, KafkaTemplate kafkaTemplate,
      ErrorHandler errorHandler) {
    factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setStatefulRetry(true);
    factory.setRetryTemplate(getRetryTemplate());
    factory.setErrorHandler(errorHandler);
    factory.setRecoveryCallback(context -> {
      ConsumerRecord<String,Product> failedData = (ConsumerRecord<String, Product>) context.getAttribute("record");
      Object data = context.getAttribute("record");
      System.out.println("Total Retry attempts for product :" +failedData +"is count ::"+context.getRetryCount() +" . Lets push to DLQ");
      kafkaTemplate.send(DLQ,failedData.key(), failedData.value());
      return null;
    });
  }

  public ConcurrentKafkaListenerContainerFactory<String, Product> getFactory() {
    return factory;
  }
  public RetryTemplate getRetryTemplate() {
    RetryTemplate template = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(10000); // in milliseconds
    backOffPolicy.setMultiplier(10);
    template.setBackOffPolicy(backOffPolicy);

    template.setRetryPolicy(getSimpleRetryPolicy());
    return template;
  }

  private SimpleRetryPolicy getSimpleRetryPolicy() {
    Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
    exceptionMap.put(IllegalArgumentException.class, false);
    exceptionMap.put(ParseException.class,false);
    exceptionMap.put(TimeoutException.class, true);
    exceptionMap.put(RetryableException.class,true);
    return new SimpleRetryPolicy(3,exceptionMap,true);
  }

}
