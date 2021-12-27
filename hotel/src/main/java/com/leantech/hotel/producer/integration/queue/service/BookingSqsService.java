package com.leantech.hotel.producer.integration.queue.service;

import com.leantech.hotel.producer.booking.domain.dto.BookingDto;

public interface BookingSqsService {

  /**
   * Send message to queue
   * @param bookingDto
   */
  void send(BookingDto bookingDto);
}
