package com.philips.downstreamservicems.config;

import com.philips.downstreamservicems.exception.RetryableException;
import com.philips.downstreamservicems.listener.TopicListenerContainer.ProductTopicListenerContainer;
import com.philips.downstreamservicems.listener.TopicListenerContainer.RetryTopicListenerContainer;
import com.philips.model.Product;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@EnableKafka
@Configuration
public class KafkaConfiguration {

  public static final int MAX_ATTEMPTS = 9;
  public static final String DLQ = "dead-letter-q";

  @Value("${bootstrap.kafka.server}")
  private String bootStrapKafkaServer;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public KafkaTemplate<String, Product> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, Product> producerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapKafkaServer);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public ConsumerFactory<String, Product> consumerFactory() {
    Map<String,Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapKafkaServer);
    config.put(ConsumerConfig.GROUP_ID_CONFIG,"supply-chain-1");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<String,Product>(config,new StringDeserializer(),new JsonDeserializer<>(Product.class));
  }

  private SimpleRetryPolicy getSimpleRetryPolicy() {
    Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
    exceptionMap.put(IllegalArgumentException.class, false);
    exceptionMap.put(ParseException.class,false);
    exceptionMap.put(TimeoutException.class, true);
    exceptionMap.put(RetryableException.class,true);
    return new SimpleRetryPolicy(MAX_ATTEMPTS,exceptionMap,true);
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate template = new RetryTemplate();

    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(1000); // in milliseconds
    template.setBackOffPolicy(backOffPolicy);

    template.setRetryPolicy(getSimpleRetryPolicy());
    return template;
  }

  @Bean
  public ErrorHandler errorHandler(KafkaTemplate<String, Product> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
        (r, e) -> new TopicPartition(DLQ, r.partition()));
    return new ExtendedSeekToCurrentErrorHandler(recoverer,
        Collections.singletonMap(RetryableException.class, true), true);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String,Product> productTopicListenerContainerFactory() {
    ProductTopicListenerContainer productTopicListenerContainer =
        new  ProductTopicListenerContainer(consumerFactory(), kafkaTemplate(), errorHandler(kafkaTemplate()));
    return productTopicListenerContainer.getFactory();

  }
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String,Product> retryTopicListenerContainerFactory() {
    RetryTopicListenerContainer retryTopicListenerContainer =
        new  RetryTopicListenerContainer(consumerFactory(), kafkaTemplate(), errorHandler(kafkaTemplate()));
    return retryTopicListenerContainer.getFactory();
  }
}
