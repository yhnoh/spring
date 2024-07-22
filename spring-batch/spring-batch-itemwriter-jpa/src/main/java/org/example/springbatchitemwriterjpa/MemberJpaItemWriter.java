package org.example.springbatchitemwriterjpa;

import jakarta.persistence.EntityManagerFactory;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MemberJpaItemWriter extends JpaItemWriter<MemberJpaEntity> {

    public MemberJpaItemWriter(@Qualifier("memberEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        super.setEntityManagerFactory(entityManagerFactory);

    }
}
