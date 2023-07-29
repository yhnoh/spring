package com.example.springbatchitemreader.item_stream;

import com.example.springbatchitemreader.BatchTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = {BatchTestConfig.class, ItemStreamFlowJobConfig.class})
@Import(BatchTestConfig.class)
@SpringBatchTest
class ItemStreamFlowJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @BeforeEach
    void beforeEach() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void itemStreamFlowJobConfigTest() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();


    }
}