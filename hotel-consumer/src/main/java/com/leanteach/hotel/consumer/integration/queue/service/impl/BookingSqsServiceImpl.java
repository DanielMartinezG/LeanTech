package com.leanteach.hotel.consumer.integration.queue.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;
import com.leanteach.hotel.consumer.booking.service.BookingService;
import com.leanteach.hotel.consumer.integration.queue.service.BookingQueueService;
import com.leanteach.hotel.consumer.integration.exception.QueueException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@EnableSqs
@Component
@Slf4j
public class BookingSqsServiceImpl implements BookingQueueService {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private BookingService bookingService;

  public void process(String message) {
    log.info("Method BookingSqsServiceServiceImpl.processMessage() message: " + message);
    try {
      BookingDto bookingDto = objectMapper.readValue(message, BookingDto.class);
      bookingService.save(bookingDto);

    } catch (JsonProcessingException e) {
      throw new QueueException("An error has occurred while mapping the message to SavingBookingDto", e);
    } catch (BookingValidationException e) {
      log.error("Validation Error", e);
    }
  }
}
