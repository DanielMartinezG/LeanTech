package com.leantech.hotel.booking.service;

import com.leantech.hotel.booking.domain.dto.BookingDto;
import java.util.List;

public interface BookingService {

  /**
   * This method send the Booking to the sqs queue
   *
   * @param bookingDto
   * @return
   */
  String process(BookingDto bookingDto);

  /**
   * This method returns all the reservations from DB
   *
   * @return
   */
  List<BookingDto> findAll();

  /**
   * Get a booking by id
   */
  BookingDto findById(Long id);

}
