package com.example.springtestcontainer.repository;

import com.example.springtestcontainer.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;


//String username, String password
@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberJpaRepositoryTest {

    @Container
    private static final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void saveTest(){
        Member member = Member.createMember("username", "password");
        Member saveMember = memberJpaRepository.save(member);

        assertThat(saveMember.getUsername()).isEqualTo("username");
        assertThat(saveMember.getPassword()).isEqualTo("password");

    }
}