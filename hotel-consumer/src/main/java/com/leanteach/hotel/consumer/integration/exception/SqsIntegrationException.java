package com.leanteach.hotel.consumer.integration.exception;

public class SqsIntegrationException extends RuntimeException {

  public SqsIntegrationException(String message) {
    super(message);
  }

  public SqsIntegrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
