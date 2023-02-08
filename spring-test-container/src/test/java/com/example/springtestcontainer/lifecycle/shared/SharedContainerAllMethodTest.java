package com.example.springtestcontainer.lifecycle.shared;

import com.example.springtestcontainer.entity.Member;
import com.example.springtestcontainer.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


//String username, String password
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SharedContainerAllMethodTest {

    //정적 필드로 선언된 컨테이너는 테스트 메서드 간에 공유
    //첫 TestCase가 실행되기 전에 컨테이너를 실행하고 마지막 TestCase가 실행된 후 컨테이너 실행 중지
    @Container
    private static final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

//    @Container
//    private final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void setup(){
        Member member = Member.createMember("username", "password");
        memberJpaRepository.save(member);
    }

    @Test
    @DisplayName("데이터 공유 확인")
    public void shareContainerVolume1Test(){
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("데이터 공유 확인")
    public void shareContainerVolume2Test(){
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findByUsernameTest(){
        Member findMember = memberJpaRepository.findByUsername("username").get();
        assertThat(findMember.getUsername()).isEqualTo("username");
    }

    @Test
    public void changeUsernameTest(){
        Member findMember = memberJpaRepository.findByUsername("username").get();
        findMember.changeUsername("username2");
        assertThat(findMember.getUsername()).isEqualTo("username2");
    }

    @Nested
    @DisplayName("")
    public class NestedTestCase{
        @Test
        public void findAllTest(){
            List<Member> members = memberJpaRepository.findAll();
            assertThat(members.size()).isEqualTo(1);
        }
    }
}