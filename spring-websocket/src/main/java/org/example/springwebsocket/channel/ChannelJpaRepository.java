package org.example.springwebsocket.channel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelJpaRepository extends JpaRepository<ChannelJpaEntity, Long> {

    List<ChannelJpaEntity> findAllByCreatedBy(long memberId);
}
