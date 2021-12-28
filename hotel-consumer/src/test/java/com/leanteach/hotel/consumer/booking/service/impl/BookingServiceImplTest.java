package com.leanteach.hotel.consumer.booking.service.impl;

import static org.mockito.BDDMockito.given;

import com.leanteach.hotel.consumer.booking.dao.BookingDao;
import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;
import com.leanteach.hotel.consumer.booking.mapper.BookingMapper;
import com.leanteach.hotel.consumer.entity.Booking;
import com.leanteach.hotel.consumer.entity.Person;
import com.leanteach.hotel.consumer.entity.Room;
import com.leanteach.hotel.consumer.integration.mail.MailSenderService;
import com.leanteach.hotel.consumer.room.service.RoomService;
import com.leanteach.hotel.consumer.util.Messages;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
  private RoomService roomService;

  @Mock
  private Validator validator;

  @Mock
  private MailSenderService mailSenderService;

  @Mock
  private Messages messages;

  private BookingServiceImpl bookingservice;


  @BeforeEach
  public void setup() {
    BookingServiceImpl bookingService = new BookingServiceImpl();
    bookingService.setBookingDao(bookingDao);
    bookingService.setBookingMapper(bookingMapper);
    bookingService.setMailSenderService(mailSenderService);
    bookingService.setValidator(validator);
    bookingService.setRoomService(roomService);
    bookingService.setMessages(messages);
    bookingService.setDefaultMailTo("danielmartinezg95@gmail.com");

    this.bookingservice = bookingService;
  }

  @Test
  public void saveSuccessFlow() throws BookingValidationException {
    LocalDate tomorrowDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(tomorrowDate)
        .fechaSalida(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .numeroHabitaciones(1)
        .totalDias(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    Set<Room> roomSet = new HashSet<>(Arrays.asList(Room.builder().capacity(5).name("queen").build()));
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(tomorrowDate)
        .dateFrom(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Set<ConstraintViolation<BookingDto>> violations = new HashSet<>();

    given(this.validator.validate(bookingDto)).willReturn(violations);
    given(this.bookingMapper.toBooking(bookingDto)).willReturn(booking);
    given(this.roomService.findByDatesAndCapacity(bookingDto)).willReturn(new ArrayList<>(roomSet));

    bookingservice.save(bookingDto);

    Mockito.verify(bookingDao, Mockito.times(1)).save(booking);

  }

  @Test
  public void saveBookingForAPastDate() {

    LocalDate yesterdayDate = LocalDate.now().plus(-1, ChronoUnit.DAYS);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(yesterdayDate)
        .fechaSalida(yesterdayDate.plus(5, ChronoUnit.DAYS))
        .numeroHabitaciones(1)
        .totalDias(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    Set<Room> roomSet = new HashSet<>(Arrays.asList(Room.builder().capacity(5).name("queen").build()));
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(yesterdayDate)
        .dateFrom(yesterdayDate.plus(5, ChronoUnit.DAYS))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Set<ConstraintViolation<BookingDto>> violations = new HashSet<>();

    given(this.validator.validate(bookingDto)).willReturn(violations);
    given(this.bookingMapper.toBooking(bookingDto)).willReturn(booking);
    given(this.roomService.findByDatesAndCapacity(bookingDto)).willReturn(new ArrayList<>(roomSet));


    Assertions.assertThrows(BookingValidationException.class, ()-> {
      bookingservice.save(bookingDto);
    });
  }

  @Test
  public void saveNotRoomsAvailable() {
    LocalDate tomorrowDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(tomorrowDate)
        .fechaSalida(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .numeroHabitaciones(1)
        .totalDias(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    Set<Room> roomSet = new HashSet<>();
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(tomorrowDate)
        .dateFrom(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Set<ConstraintViolation<BookingDto>> violations = new HashSet<>();

    given(this.validator.validate(bookingDto)).willReturn(violations);
    given(this.bookingMapper.toBooking(bookingDto)).willReturn(booking);
    given(this.roomService.findByDatesAndCapacity(bookingDto)).willReturn(new ArrayList<>(roomSet));


    Assertions.assertThrows(BookingValidationException.class, ()-> {
      bookingservice.save(bookingDto);
    });
  }

  @Test
  public void saveDateToBeforeDateFrom() {
    LocalDate tomorrowDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .fechaSalida(tomorrowDate)
        .numeroHabitaciones(1)
        .totalDias(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    Set<Room> roomSet = new HashSet<>();
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(tomorrowDate)
        .dateFrom(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Set<ConstraintViolation<BookingDto>> violations = new HashSet<>();

    given(this.validator.validate(bookingDto)).willReturn(violations);
    given(this.bookingMapper.toBooking(bookingDto)).willReturn(booking);
    given(this.roomService.findByDatesAndCapacity(bookingDto)).willReturn(new ArrayList<>(roomSet));


    Assertions.assertThrows(BookingValidationException.class, ()-> {
      bookingservice.save(bookingDto);
    });
  }

  @Test
  public void saveRoomNumberDoesntMatch() {
    LocalDate tomorrowDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(tomorrowDate)
        .fechaSalida(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .numeroHabitaciones(1)
        .totalDias(4)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    Set<Room> roomSet = new HashSet<>();
    Person reservationHolder = Person.builder().name("Daniel Martinez").build();

    Booking booking = Booking.builder()
        .numberOfPeople(5)
        .dateTo(tomorrowDate)
        .dateFrom(tomorrowDate.plus(5, ChronoUnit.DAYS))
        .room(roomSet)
        .numberOfPeople(5)
        .numberOfChilds(0)
        .reservationHolder(reservationHolder)
        .build();

    Set<ConstraintViolation<BookingDto>> violations = new HashSet<>();

    given(this.validator.validate(bookingDto)).willReturn(violations);
    given(this.bookingMapper.toBooking(bookingDto)).willReturn(booking);
    given(this.roomService.findByDatesAndCapacity(bookingDto)).willReturn(new ArrayList<>(roomSet));


    Assertions.assertThrows(BookingValidationException.class, ()-> {
      bookingservice.save(bookingDto);
    });
  }


}
