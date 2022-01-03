package com.leantech.hotel.booking.dao;

import com.leantech.hotel.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDao extends CrudRepository<Booking, Long> {


}
