package com.example.springbatchtest.Integration;

import com.example.springbatchtest.DataJobConfig;
import com.example.springbatchtest.TestBatchConfig;
import com.example.springbatchtest.entity.DailyStockPrice;
import com.example.springbatchtest.repository.DailyStockPriceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void beforeEach(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @BeforeEach
    void afterEach(){
        dailyStockPriceEntityRepository.deleteAll();
    }

    @Test
    void launchJobTest() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(99);
    }

    @Test
    void launchJobTest2() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(99);

    }

}
