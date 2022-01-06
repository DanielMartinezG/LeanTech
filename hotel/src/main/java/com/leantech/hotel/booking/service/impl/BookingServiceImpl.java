package com.leantech.hotel.booking.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.amazonaws.util.StringUtils;
import com.leantech.hotel.booking.dao.BookingDao;
import com.leantech.hotel.booking.domain.dto.BookingDto;
import com.leantech.hotel.booking.exception.BookingException;
import com.leantech.hotel.booking.mapper.BookingMapper;
import com.leantech.hotel.booking.service.BookingService;
import com.leantech.hotel.entity.Booking;
import com.leantech.hotel.integration.queue.service.BookingSqsService;
import com.leantech.hotel.util.Messages;
import java.util.List;
import java.util.Optional;
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

  @Override
  public BookingDto findById(Long id) {
    log.info("Method BookingServiceImpl.findById()");
    Optional<Booking> booking = bookingDao.findById(id);
    if (!booking.isEmpty()) {
      return mapToBookingDto(booking.get());
    }
    throw new BookingException(message.getMessages("booking.not.found"));
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
    bookingDto.setEmail(getEmail(booking));

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
   * fill the field "email" from booking
   *
   * @param booking
   * @return
   */
  private String getEmail(Booking booking) {
    log.info("Method BookingServiceImpl.getEmail() booking Object:" + booking);
    return booking != null && booking.getReservationHolder() != null && !StringUtils.isNullOrEmpty(booking.getReservationHolder().getEmail())? booking.getReservationHolder().getEmail() : "";
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
