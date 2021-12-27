package com.leantech.hotel.producer.config.sentry;

import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SentryService {

  @Value("${sentry.dns}")
  private String dns;

  /**
   * Send the exception to sentry
   * @param e
   */
  public void capture(Exception e) {
    Sentry.init(dns);
    Sentry.capture(e);
  }

}
