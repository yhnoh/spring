package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.TestBatchConfig;
import com.example.springbatchdatabase.entity.Member;
import com.example.springbatchdatabase.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, HibernateCursorJobConfig.class})
public class HibernateCursorJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MemberJpaRepository memberJpaRepository;


    @BeforeEach
    void setup() {
        jobRepositoryTestUtils.removeJobExecutions();
        memberJpaRepository.deleteAll();
    }

    @Test
    public void launchJobTest() throws Exception {

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = Member.builder()
                    .username("username" + i)
                    .build();
            members.add(member);
        }
        memberJpaRepository.saveAll(members);

        jobLauncherTestUtils.launchJob();
    }
}
