package org.example.springwebsocket.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelUseCase channelUseCase;

    @PostMapping("/channels")
    public void createChannel(@RequestBody ChannelCreateRequest request) {
        channelUseCase.createChannel(request.getMemberId(), request.getChannelName());
    }

    @PostMapping("/channel-participants")
    public void addChannelParticipants(@RequestBody ChannelParticipantAddRequest request) {
        channelUseCase.addChannelParticipant(request.getChannelId(), request.getMemberId());
    }


}
