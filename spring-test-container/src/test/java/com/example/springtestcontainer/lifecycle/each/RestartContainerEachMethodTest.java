package com.example.springtestcontainer.lifecycle.each;

import com.example.springtestcontainer.entity.Member;
import com.example.springtestcontainer.repository.MemberJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RestartContainerEachMethodTest {

    //인스턴스 필드로 선언된 컨테이너는 테스트가 실행될때마다 컨테이너 실행 및 중지가 계속해서 일어난다.
    @Container
    private final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void setup(){
        Member member = Member.createMember("username", "password");
        memberJpaRepository.save(member);
    }

    @Test
    public void testCase1(){
        Member findMember = memberJpaRepository.findByUsername("username").get();
        assertThat(findMember.getUsername()).isEqualTo("username");
    }

    @Test
    public void testCase2(){
        Member findMember = memberJpaRepository.findByUsername("username").get();
        assertThat(findMember.getUsername()).isEqualTo("username");
    }

}
