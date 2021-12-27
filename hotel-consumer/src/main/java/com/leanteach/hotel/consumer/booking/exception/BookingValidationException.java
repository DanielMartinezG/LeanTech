package com.leanteach.hotel.consumer.booking.exception;

public class BookingValidationException extends Exception {

  public BookingValidationException(String message) {
    super(message);
  }

  public BookingValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
