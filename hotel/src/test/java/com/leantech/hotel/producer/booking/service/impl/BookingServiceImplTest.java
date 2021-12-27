package com.leantech.hotel.producer.booking.service.impl;

import static org.mockito.BDDMockito.given;

import com.leantech.hotel.producer.booking.dao.BookingDao;
import com.leantech.hotel.producer.booking.domain.dto.BookingDto;
import com.leantech.hotel.producer.booking.mapper.BookingMapper;
import com.leantech.hotel.producer.entity.Booking;
import com.leantech.hotel.producer.entity.Person;
import com.leantech.hotel.producer.entity.Room;
import com.leantech.hotel.producer.integration.queue.service.BookingSqsService;
import com.leantech.hotel.producer.util.Messages;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BookingServiceImplTest {

  @Mock
  private BookingDao bookingDao;

  @Mock
  private BookingMapper bookingMapper;

  @Mock
  private BookingSqsService bookingSqsService;

  @Mock
  private Messages messages;

  private BookingServiceImpl bookingservice;


  @BeforeEach
  public void setup() {
    this.bookingservice = new BookingServiceImpl(bookingDao, bookingMapper, bookingSqsService, messages);
  }

  @Test
  public void processSuccessFlow() {
    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(LocalDate.of(2021, 12, 25))
        .fechaSalida(LocalDate.of(2021, 12, 30))
        .numeroHabitaciones(1)
        .numeroPersonas(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();

    bookingservice.process(bookingDto);

    Mockito.verify(bookingSqsService, Mockito.times(1)).send(bookingDto);
  }

  @Test
  public void findAllSuccessFlow() {
    Set<Room> roomSet = new HashSet<>(Arrays.asList(Room.builder().capacity(2).name("queen").build()));
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(LocalDate.of(2021, 12, 25))
        .dateFrom(LocalDate.of(2021, 12, 30))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Iterable<Booking> bookingDtoIterable = Arrays.asList(booking);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(LocalDate.of(2021, 12, 25))
        .fechaSalida(LocalDate.of(2021, 12, 30))
        .numeroHabitaciones(1)
        .numeroPersonas(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();

    given(this.bookingDao.findAll()).willReturn(bookingDtoIterable);
    given(this.bookingMapper.toBookingDto(booking)).willReturn(bookingDto);

    List<BookingDto> bookingDtoList = bookingservice.findAll();

    Assertions.assertEquals(bookingDtoList.get(0).getTitularReserva(), reservationHolder.getName());
  }
}
