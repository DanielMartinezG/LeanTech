package com.leantech.hotel.producer.booking.dao;

import com.leantech.hotel.producer.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDao extends CrudRepository<Booking, Long> {


}
