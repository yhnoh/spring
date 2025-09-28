package org.example.springwebsocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class SendChatMessage {

    private String message;
    private LocalDateTime sendChatAt = LocalDateTime.now();

}
