package com.leanteach.hotel.consumer.integration.mail;

import com.leanteach.hotel.consumer.config.aws.domain.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

  @Autowired
  private MailSender mailSender;

  @Value("${cloud.aws.mail-from}")
  private String mailFrom;

  public void sendEmail(MailDto mailDto) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(mailFrom);
    simpleMailMessage.setTo(mailDto.getTo());
    simpleMailMessage.setSubject(mailDto.getSubject());
    simpleMailMessage.setText(mailDto.getBody());
    mailSender.send(simpleMailMessage);
  }
}
