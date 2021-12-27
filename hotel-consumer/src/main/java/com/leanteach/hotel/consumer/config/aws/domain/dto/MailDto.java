package com.leanteach.hotel.consumer.config.aws.domain.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MailDto implements Serializable {
    private static final long serialVersionUID = -8755819342178690638L;

    private String subject;
    private String from;
    private String to;
    private String body;

}
