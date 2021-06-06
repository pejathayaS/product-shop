package com.philips.downstreamservicems.service;

import com.philips.downstreamservicems.listener.DeadLetterQueueConsumer;
import com.philips.model.Product;
import java.time.Duration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerJob implements Runnable {

  @Autowired
  private CommunicationService communicationService;

  public static final Boolean running = true;

  public void run() {
    Consumer<String, Product> consumer = DeadLetterQueueConsumer.createConsumer();
    try {
      while (running) {
        ConsumerRecords<String, Product> records = consumer.poll(Duration.ofMinutes(1));
        for (ConsumerRecord<String, Product> record : records) {
          System.out.println("Processing from DLQ");
          communicationService.processData(record.key(), record.value());
        }
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      consumer.close();
    }
  }

}


