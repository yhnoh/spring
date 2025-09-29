package org.example.springwebsocket.channel;

import lombok.RequiredArgsConstructor;
import org.example.springwebsocket.member.MemberJpaEntity;
import org.example.springwebsocket.member.MemberJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChannelUseCase {

    private final MemberJpaRepository memberJpaRepository;
    private final ChannelJpaRepository channelJpaRepository;
    private final ChannelParticipantJpaRepository channelParticipantJpaRepository;

    //채널을 생성한다.
    public void createChannel(long memberId, String channelName) {

        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ChannelJpaEntity channel = new ChannelJpaEntity();
        channel.setName(channelName);
        channel.setCreatedBy(memberId);
        channel.setCreatedAt(LocalDateTime.now());
        channel.setModifiedBy(memberId);
        channel.setModifiedAt(LocalDateTime.now());
        channelJpaRepository.save(channel);

        ChannelParticipantJpaEntity channelParticipant = new ChannelParticipantJpaEntity();
        channelParticipant.setChnnel(channel);
        channelParticipant.setOwner(true);
        channelParticipant.setMemberId(memberId);
        channelParticipant.setCreatedAt(LocalDateTime.now());
        channelParticipantJpaRepository.save(channelParticipant);
    }

    public void addChannelParticipant(long channelId, long memberId) {

        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ChannelJpaEntity channel = channelJpaRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        ChannelParticipantJpaEntity channelParticipant = new ChannelParticipantJpaEntity();
        channelParticipant.setChnnel(channel);
        channelParticipant.setOwner(false);
        channelParticipant.setMemberId(memberId);
        channelParticipant.setCreatedAt(LocalDateTime.now());
        channelParticipantJpaRepository.save(channelParticipant);
    }

}
