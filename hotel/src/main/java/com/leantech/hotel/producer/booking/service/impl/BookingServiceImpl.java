package com.leantech.hotel.producer.booking.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.leantech.hotel.producer.booking.dao.BookingDao;
import com.leantech.hotel.producer.booking.domain.dto.BookingDto;
import com.leantech.hotel.producer.booking.mapper.BookingMapper;
import com.leantech.hotel.producer.booking.service.BookingService;
import com.leantech.hotel.producer.entity.Booking;
import com.leantech.hotel.producer.integration.queue.service.BookingSqsService;
import com.leantech.hotel.producer.util.Messages;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

  @Autowired
  private BookingDao bookingDao;

  @Autowired
  private BookingMapper bookingMapper;

  @Autowired
  private BookingSqsService bookingSqsService;

  @Autowired
  private Messages message;

  @Override
  public String process(BookingDto bookingDto) {
    log.info("Method BookingServiceImpl.process() bookingDto: " + bookingDto);

    bookingSqsService.send(bookingDto);

    log.info("Booking sent to the queue successfully");
    return message.getMessages("booking.save.success");
  }

  @Override
  public List<BookingDto> findAll() {
    log.info("Method BookingServiceImpl.getAll()");
    return StreamSupport.stream(bookingDao.findAll().spliterator(), false)
        .map(booking -> mapToBookingDto(booking))
        .collect(Collectors.toList());
  }

  /**
   * Map booking to dto filling the number of rooms and the reservation holder
   *
   * @param booking
   * @return
   */
  private BookingDto mapToBookingDto(Booking booking) {
    log.info("Method BookingServiceImpl.mapToBookingDto() booking Object:" + booking);
    BookingDto bookingDto = bookingMapper.toBookingDto(booking);
    bookingDto.setNumeroHabitaciones(getNumeroHabitaciones(booking));
    bookingDto.setTitularReserva(getTitularReserva(booking));

    return bookingDto;
  }

  /**
   * fill the field "titularReserva" from booking
   *
   * @param booking
   * @return
   */
  private String getTitularReserva(Booking booking) {
    log.info("Method BookingServiceImpl.getTitularReserva() booking Object:" + booking);
    return booking != null && booking.getReservationHolder() != null ? booking.getReservationHolder().getName() : "";
  }

  /**
   * fill the field "numeroHabitaciones" from booking
   *
   * @param booking
   * @return
   */
  private int getNumeroHabitaciones(Booking booking) {
    log.info("Method BookingServiceImpl.getNumeroHabitaciones() booking Object:" + booking);
    return booking != null && !CollectionUtils.isNullOrEmpty(booking.getRoom()) ? booking.getRoom().size() : 0;
  }
}
