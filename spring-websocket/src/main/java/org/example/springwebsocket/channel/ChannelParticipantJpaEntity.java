package org.example.springwebsocket.channel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "member_id")
    private long memberId;

}
