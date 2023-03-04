package com.example.springbatchtest.Integration;

import com.example.springbatchtest.DataJobConfig;
import com.example.springbatchtest.TestBatchConfig;
import com.example.springbatchtest.entity.DailyStockPrice;
import com.example.springbatchtest.repository.DailyStockPriceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, DataJobConfig.class})
@ActiveProfiles("test")
class DailJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private DailyStockPriceEntityRepository dailyStockPriceEntityRepository;

    @BeforeEach
    void setup(){
        jobRepositoryTestUtils.removeJobExecutions();
    }
    @Test
    void launchJobTest() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
    }

}
