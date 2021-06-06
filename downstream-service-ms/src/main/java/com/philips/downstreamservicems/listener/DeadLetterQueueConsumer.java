package com.philips.downstreamservicems.listener;

import com.philips.model.Product;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class DeadLetterQueueConsumer {

  public static final String DLQ = "dead-letter-q";

  @Value("${bootstrap.kafka.server}")
  private String bootStrapKafkaServer;

  public static Consumer<String, Product> createConsumer() {
    final Properties props = new Properties();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG,DLQ);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
    final Consumer<String, Product> consumer =
        new KafkaConsumer<>(props);

    consumer.subscribe(Collections.singletonList(DLQ));
    return consumer;
  }
}
