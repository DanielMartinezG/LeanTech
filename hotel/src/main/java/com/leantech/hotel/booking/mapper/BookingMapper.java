package com.leantech.hotel.booking.mapper;

import com.amazonaws.util.CollectionUtils;
import com.leantech.hotel.booking.domain.dto.BookingDto;
import com.leantech.hotel.entity.Booking;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, uses = {Booking.class, BookingDto.class})
public abstract class BookingMapper {

  @Mapping(source = "dateFrom", target = "fechaIngreso")
  @Mapping(source = "dateTo", target = "fechaSalida")
  @Mapping(source = "numberOfChilds", target = "numeroMenores")
  @Mapping(source = "numberOfPeople", target = "numeroPersonas")
  @Mapping(source = "totalDays", target = "totalDias")
  @Mapping(target = "numeroHabitaciones", qualifiedByName = "getNumeroHabitaciones", ignore = true)
  @Mapping(target = "titularReserva", qualifiedByName = "getTitularReserva", ignore = true)
  @Mapping(target = "email", ignore = true)
  public abstract BookingDto toBookingDto(Booking booking);

  @Named("getTitularReserva")
  private String getTitularReserva(Booking booking) {
    return booking != null && booking.getReservationHolder() != null ? booking.getReservationHolder().getName() : "";
  }

  @Named("getNumeroHabitaciones")
  private int getNumeroHabitaciones(Booking booking) {
    return booking != null && !CollectionUtils.isNullOrEmpty(booking.getRoom())? booking.getRoom().size() : 0;
  }
}
