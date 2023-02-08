package com.example.springtestcontainer.lifecycle.singleton;

import com.example.springtestcontainer.entity.Member;
import com.example.springtestcontainer.repository.MemberJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SingletonContainerTest1 extends AbstractSingletonContainerTest{


    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @BeforeEach
    public void setup(){
        Member member = Member.createMember("username", "password");
        memberJpaRepository.save(member);
    }

    @Test
    public void testcase1(){
        List<Member> members = memberJpaRepository.findAll();
        Assertions.assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void testcase2(){
        List<Member> members = memberJpaRepository.findAll();
        Assertions.assertThat(members.size()).isEqualTo(1);
    }


}
