package com.example.mapstruct.repository;

import com.example.mapstruct.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void test() {
        Member username = Member.createMember("username");
        Member savedMember = memberJpaRepository.save(username);

        assertThat(savedMember.getUsername()).isEqualTo("username");
    }
}