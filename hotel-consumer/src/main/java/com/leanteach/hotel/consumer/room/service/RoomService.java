package com.leanteach.hotel.consumer.room.service;

import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.entity.Room;
import java.util.List;

public interface RoomService {

  /**
   * This method returns a list with the rooms availables to the dates you set as parameters
   *
   * @param bookingDto
   * @return
   */
  List<Room> findByDatesAndCapacity(BookingDto bookingDto);
}
