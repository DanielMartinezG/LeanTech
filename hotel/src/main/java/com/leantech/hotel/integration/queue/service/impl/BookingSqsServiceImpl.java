package com.leantech.hotel.integration.queue.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leantech.hotel.booking.domain.dto.BookingDto;
import com.leantech.hotel.booking.exception.BookingException;
import com.leantech.hotel.integration.queue.service.BookingSqsService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Setter
@NoArgsConstructor
@Service
@Slf4j
public class BookingSqsServiceImpl implements BookingSqsService {

  @Autowired
  private QueueMessagingTemplate queueMessagingTemplate;

  @Value("${cloud.aws.end-point.uri}")
  private String sqsBookingEndPoint;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Send the BookingDto object to
   *
   * @param bookingDto
   */
  public void send(BookingDto bookingDto) {
    log.info("Method BookingSqsService.sendSaveBookingDtoToSqs() bookingDto: " + bookingDto);
    try {
      queueMessagingTemplate.send(sqsBookingEndPoint, MessageBuilder.withPayload(objectMapper.writeValueAsString(bookingDto)).build());
    } catch (JsonProcessingException e) {
      throw new BookingException("An error has occurred parsing the object to send it to the queue", e);
    }
  }

}
