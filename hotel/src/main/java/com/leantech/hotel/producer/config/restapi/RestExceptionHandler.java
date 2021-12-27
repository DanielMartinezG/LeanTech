package com.leantech.hotel.producer.config.restapi;

import com.leantech.hotel.producer.booking.exception.BookingException;
import com.leantech.hotel.producer.config.sentry.SentryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private SentryService sentryService;

  @ExceptionHandler(BookingException.class)
  protected ResponseEntity<Object> handleBookingException(BookingException ex) {
    log.error(ex.getMessage(), ex);
    sentryService.capture(ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler the exception in order to avoid showing error traces from the server to the user
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);
    sentryService.capture(ex);
    return new ResponseEntity<>("Ha ocurrido un error, por favor intente más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
