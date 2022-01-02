package com.leanteach.hotel.consumer.booking.dao;

import com.leanteach.hotel.consumer.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDao extends CrudRepository<Booking, Long> {


}
