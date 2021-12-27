package com.leantech.hotel.producer.booking.dao;

import com.leantech.hotel.producer.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingDao extends CrudRepository<Booking, Long> {


}
