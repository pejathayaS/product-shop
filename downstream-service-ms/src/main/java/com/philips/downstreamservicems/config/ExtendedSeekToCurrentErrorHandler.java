package com.philips.downstreamservicems.config;


import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.retry.annotation.Backoff;
import org.springframework.util.backoff.BackOff;

public class ExtendedSeekToCurrentErrorHandler extends SeekToCurrentErrorHandler {

  private final BiConsumer<ConsumerRecord<?, ?>, Exception> recoverer;
  private final BinaryExceptionClassifier retryableClassifier;

  public ExtendedSeekToCurrentErrorHandler(
      BiConsumer<ConsumerRecord<?, ?>, Exception> recoverer,
      Map<Class<? extends Throwable>, Boolean> retryableExceptions,
      boolean traverseCauses
  ) {
    super(recoverer);
    this.recoverer = recoverer;
    this.retryableClassifier = new BinaryExceptionClassifier(retryableExceptions, false);
    this.retryableClassifier.setTraverseCauses(traverseCauses);
  }

  @Override
  public void handle(Exception thrownException,List<ConsumerRecord<?, ?>> records,
      Consumer<?, ?> consumer,
      MessageListenerContainer container) {
    if (isRetryable(thrownException)) {
      super.handle(thrownException, records, consumer, container);
    } else if (!records.isEmpty()) {
      recoverer.accept(records.get(0), thrownException);
    }
  }

  private boolean isRetryable(Throwable throwable) {
    return retryableClassifier.classify(throwable);
  }
}
