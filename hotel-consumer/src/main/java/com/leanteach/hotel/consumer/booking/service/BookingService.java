package com.leanteach.hotel.consumer.booking.service;

import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;

public interface BookingService {

  /**
   * This method save the booking to the DB
   * @param bookingDto
   * @return
   */
  void save(BookingDto bookingDto) throws BookingValidationException;
}
