package org.example.springwebsocket.channel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelCreateRequest {

    private long memberId;
    private String channelName;
}
