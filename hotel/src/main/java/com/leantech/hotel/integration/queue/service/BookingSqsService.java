package com.leantech.hotel.integration.queue.service;

import com.leantech.hotel.booking.domain.dto.BookingDto;

public interface BookingSqsService {

  /**
   * Send message to queue
   * @param bookingDto
   */
  void send(BookingDto bookingDto);
}
