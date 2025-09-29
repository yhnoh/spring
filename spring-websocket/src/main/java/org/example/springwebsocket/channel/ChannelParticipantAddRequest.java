package org.example.springwebsocket.channel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelParticipantAddRequest {
    private long channelId;
    private long memberId;
}
