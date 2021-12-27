package com.leanteach.hotel.consumer.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "booking")
public class Booking implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "date_from", nullable = false)
  private LocalDate dateFrom;
  @Column(name = "date_to", nullable = false)
  private LocalDate dateTo;
  @Column(name = "total_days")
  private Integer totalDays;
  @Column(name = "number_of_people", nullable = false)
  private Integer numberOfPeople;
  @Column(name = "number_of_childs", nullable = false)
  private Integer numberOfChilds;
  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "reservation_holder_id",  nullable = false)
  private Person reservationHolder;
  @ManyToMany(cascade = { CascadeType.MERGE })
  @JoinTable(
      name = "booking_room",
      joinColumns = { @JoinColumn(name = "booking_id") },
      inverseJoinColumns = { @JoinColumn(name = "room_id") }
  )
  private Set<Room> room;

}
