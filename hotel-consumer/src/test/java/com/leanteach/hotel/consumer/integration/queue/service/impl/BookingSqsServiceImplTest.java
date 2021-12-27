package com.leanteach.hotel.consumer.integration.queue.service.impl;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;
import com.leanteach.hotel.consumer.booking.service.BookingService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BookingSqsServiceImplTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private BookingService bookingService;

  private BookingSqsServiceImpl bookingSqsListenerServiceImpl;


  @BeforeEach
  public void setup() {
    this.bookingSqsListenerServiceImpl = new BookingSqsServiceImpl(objectMapper, bookingService);
  }

  @Test
  public void processMessageSuccessFlow() throws JsonProcessingException, BookingValidationException {
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

    String message = "{\"fechaIngreso\":\"2021-12-25\",\"fechaSalida\":\"2021-12-30\",\"totalDias\":3,\"numeroPersonas\":10,\"titularReserva\":\"Daniel Martinez\",\"numeroHabitaciones\":2,\"numeroMenores\":0,\"email\":\"mariaalejandrabaia@outlook.com\"}";

    given(objectMapper.readValue(message, BookingDto.class)).willReturn(bookingDto);

    bookingSqsListenerServiceImpl.process(message);

    Mockito.verify(bookingService, Mockito.times(1)).save(bookingDto);

  }
}
