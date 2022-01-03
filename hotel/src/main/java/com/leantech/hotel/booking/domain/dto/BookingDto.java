package com.leantech.hotel.booking.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookingDto {

  private LocalDate fechaIngreso;
  private LocalDate fechaSalida;
  private Integer totalDias;
  private Integer numeroPersonas;
  private String titularReserva;
  private Integer numeroHabitaciones;
  private Integer numeroMenores;
  private String email;

}
