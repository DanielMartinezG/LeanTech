package com.leanteach.hotel.consumer.integration.queue.listener;

import com.leanteach.hotel.consumer.integration.exception.QueueException;
import com.leanteach.hotel.consumer.integration.queue.service.BookingQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@EnableSqs
@Component
@Slf4j
public class BookingSqsListener {

  @Autowired
  private BookingQueueService bookingQueueService;

  @SqsListener(value = "${cloud.aws.booking-queue.name}")
  public void processBookingMessage(String message) {
    log.info("Method BookingListenerService.processMessage() message: " + message);
    try {
      bookingQueueService.process(message);

    } catch (QueueException e) {
      log.error("Error while reading the queue", e);
    }
  }
}
