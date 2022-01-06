package com.leantech.hotel.booking.controller;

import com.leantech.hotel.booking.domain.dto.BookingDto;
import com.leantech.hotel.booking.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Api(value = "Endpoints available to handle the booking.")
public class BookingController {

  @Autowired
  private BookingService bookingService;

  @PostMapping("/registrar-reserva")
  @ApiOperation(value = "Restful API in charge of making the reservation.", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Your reservation was sent to the queue successfully"),
      @ApiResponse(code = 400, message = "We found an error in the data you gave to us, you must check your email")
  })
  public ResponseEntity<String> save(@RequestBody BookingDto bookingDto) {
    return ResponseEntity.ok().body(bookingService.process(bookingDto));
  }

  @GetMapping("/consultar-reservas")
  @ApiOperation(value = "Restful API that return a list with all the reservations in the system.", response = List.class)
  public ResponseEntity<List<BookingDto>> findAll() {
    return ResponseEntity.ok().body(bookingService.findAll());
  }

  @GetMapping("/consultar-reserva")
  @ApiOperation(value = "Restful API that return a reservation by its id.", response = BookingDto.class)
  public ResponseEntity<BookingDto> findById(@RequestParam Long bookingId) {
    return ResponseEntity.ok().body(bookingService.findById(bookingId));
  }
}
