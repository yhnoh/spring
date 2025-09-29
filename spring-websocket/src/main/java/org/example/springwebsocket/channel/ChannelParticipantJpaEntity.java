package org.example.springwebsocket.channel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "channel_participant")
public class ChannelParticipantJpaEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private ChannelJpaEntity chnnel;


    @Column(name = "is_owner")
    private boolean isOwner;

    /**
     * 유니크 키
     */
    @Column(name = "member_id")
    private long memberId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
