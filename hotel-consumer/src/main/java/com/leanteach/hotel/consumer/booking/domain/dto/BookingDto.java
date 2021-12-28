package com.leanteach.hotel.consumer.booking.domain.dto;

import com.amazonaws.util.CollectionUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

  @NotNull(message = "La fecha de ingreso es un campo obligatorio")
  private LocalDate fechaIngreso;
  @NotNull(message = "La fecha de salida es un campo obligatorio")
  private LocalDate fechaSalida;
  @NotNull(message = "El total de días es un campo obligatorio")
  @Range(min = 1, message= "El número de dias debe ser mayor a 0")
  private Integer totalDias;
  @NotNull(message = "El número de personas es un campo obligatorio")
  @Range(min = 1, message= "El número de personas debe ser mayor a 0")
  private Integer numeroPersonas;
  @NotBlank(message = "El titular de la reserva es un campo obligatorio")
  private String titularReserva;
  @NotNull(message = "Número de habitaciones es un campo obligatorio")
  @Range(min = 1, message= "El número de habitaciones debe ser mayor a 0")
  private Integer numeroHabitaciones;
  @NotNull(message = "La cantidad de menores es un campo obligatorio")
  private Integer numeroMenores;
  @Email(message = "Email inválido")
  private String email;
  private List<String> errorList;

  public List<String> getErrorList() {
    if (CollectionUtils.isNullOrEmpty(this.errorList)) {
      return new ArrayList<>();
    }
    return this.errorList;
  }
}
