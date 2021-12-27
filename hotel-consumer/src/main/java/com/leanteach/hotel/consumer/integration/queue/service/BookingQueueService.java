package com.leanteach.hotel.consumer.integration.queue.service;

public interface BookingQueueService {

  /**
   * Process queue message
   * @param message
   */
  void process(String message);
}
