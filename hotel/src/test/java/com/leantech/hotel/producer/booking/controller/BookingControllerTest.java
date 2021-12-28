package com.leantech.hotel.producer.booking.controller;

import static org.mockito.BDDMockito.given;

import com.leantech.hotel.producer.booking.domain.dto.BookingDto;
import com.leantech.hotel.producer.booking.service.BookingService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BookingControllerTest {

  @Mock
  private BookingService bookingService;


  private BookingController bookingController;


  @BeforeEach
  public void setup() {
    this.bookingController = new BookingController(bookingService);
  }

  @Test
  public void saveBookingSuccessFlow() {
    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(LocalDate.of(2021, 12, 25))
        .fechaSalida(LocalDate.of(2021, 12, 30))
        .numeroHabitaciones(1)
        .numeroPersonas(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();

    String responseMessage = "La reserva se está procesando correctamente, pronto recibirá un correo con el resultado";

    given(this.bookingService.process(bookingDto)).willReturn(responseMessage);

    ResponseEntity<String> response = bookingController.save(bookingDto);

    Assertions.assertEquals(response.getBody(), responseMessage);
  }

  @Test
  public void findAllBookingSuccessFlow() {
    BookingDto bookingDto = BookingDto.builder()
        .numeroPersonas(5)
        .fechaIngreso(LocalDate.of(2021, 12, 25))
        .fechaSalida(LocalDate.of(2021, 12, 30))
        .numeroHabitaciones(1)
        .numeroPersonas(5)
        .numeroMenores(0)
        .titularReserva("Daniel Martínez")
        .build();
    List<BookingDto> returnList = Arrays.asList(bookingDto);

    given(this.bookingService.findAll()).willReturn(returnList);

    ResponseEntity<List<BookingDto>> response = bookingController.findAll();

    Assertions.assertEquals(response.getBody(), returnList);
  }

}
