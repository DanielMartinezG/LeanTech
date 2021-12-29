package com.leanteach.hotel.consumer.config.aws.domain.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MailDto implements Serializable {

  private static final long serialVersionUID = -8755819342178690638L;

  private String subject;
  private String from;
  private String to;
  private String body;

}
