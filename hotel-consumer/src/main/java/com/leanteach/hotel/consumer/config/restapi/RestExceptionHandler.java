package com.leanteach.hotel.consumer.config.restapi;

import com.leanteach.hotel.consumer.booking.exception.BookingException;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;
import com.leanteach.hotel.consumer.config.sentry.SentryService;
import com.leanteach.hotel.consumer.integration.exception.SqsIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
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
  protected ResponseEntity<Object> handleBookingException(BookingException e) {
    log.error(e.getMessage(), e);
    sentryService.capture(e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SqsIntegrationException.class)
  protected ResponseEntity<Object> handleSqsIntegrationException(SqsIntegrationException e) {
    log.error(e.getMessage(), e);
    sentryService.capture(e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JDBCException.class)
  protected ResponseEntity<Object> handleSqsIntegrationException(JDBCException e) {
    log.error(e.getMessage(), e);
    sentryService.capture(e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BookingValidationException.class)
  protected ResponseEntity<Object> handleSqsIntegrationException(BookingValidationException e) {
    log.error(e.getMessage(), e);
    sentryService.capture(e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler the exception in order to avoid showing error traces from the server to the user
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception e) {
    log.error(e.getMessage(), e);
    sentryService.capture(e);
    return new ResponseEntity<>("Ha ocurrido un error, por favor intente más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
