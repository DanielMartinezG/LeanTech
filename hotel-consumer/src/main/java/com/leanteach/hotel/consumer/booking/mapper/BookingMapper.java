package com.leanteach.hotel.consumer.booking.mapper;

import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.entity.Booking;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, uses = {Booking.class, BookingDto.class})
public abstract class BookingMapper {

  @Mapping(source = "fechaIngreso", target = "dateFrom")
  @Mapping(source = "fechaSalida", target = "dateTo")
  @Mapping(source = "numeroMenores", target = "numberOfChilds")
  @Mapping(source = "numeroPersonas", target = "numberOfPeople")
  @Mapping(source = "totalDias", target = "totalDays")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "room", ignore = true)
  @Mapping(target = "reservationHolder", ignore = true)
  public abstract Booking toBooking(BookingDto bookingDto);
}
