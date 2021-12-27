package com.leanteach.hotel.consumer.room.dao;

import com.leanteach.hotel.consumer.entity.Room;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomDao extends CrudRepository<Room, Long> {


  @Query("SELECT room "
      + "FROM Room room "
      + "LEFT JOIN room.booking booking "
      + "WHERE (:dateFrom < booking.dateFrom "
      + "AND :dateTo <= booking.dateFrom) "
      + "OR :dateFrom >= booking.dateTo "
      + "OR booking.id IS NULL "
      + "AND room.capacity >= :capacity "
      + "ORDER BY room.capacity ASC ")
  List<Room> findByDatesAndCapacity(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo, @Param("capacity") Integer capacity);

}
