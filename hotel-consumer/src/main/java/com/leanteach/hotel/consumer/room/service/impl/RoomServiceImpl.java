package com.leanteach.hotel.consumer.room.service.impl;

import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.service.impl.BookingServiceImpl;
import com.leanteach.hotel.consumer.entity.Room;
import com.leanteach.hotel.consumer.room.dao.RoomDao;
import com.leanteach.hotel.consumer.room.service.RoomService;
import com.leanteach.hotel.consumer.util.Messages;
import com.leanteach.hotel.consumer.util.NumberUtility;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

  @Autowired
  private RoomDao roomDao;

  @Autowired
  private Messages messages;

  @Override
  public List<Room> findByDatesAndCapacity(BookingDto bookingDto) {
    log.info("Method RoomServiceImpl.findByDatesAndCapacity() bookingDto: " + bookingDto);

    Assert.notNull(bookingDto, messages.getMessages("booking.dto.is.required"));
    Assert.isTrue(NumberUtility.isNotNullAndGreaterThanZero(bookingDto.getNumeroPersonas()), "Numero de personas is a required field");
    Assert.isTrue(NumberUtility.isNotNullAndGreaterThanZero(bookingDto.getNumeroHabitaciones()), "Numero de habitaciones is a required field");
    return roomDao.findByDatesAndCapacity(bookingDto.getFechaIngreso(), bookingDto.getFechaSalida(), Integer.valueOf(bookingDto.getNumeroPersonas() / bookingDto.getNumeroHabitaciones()));
  }
}
