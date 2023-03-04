package com.example.springbatchtest;

import com.example.springbatchtest.dto.DailyStockPriceDTO;
import com.example.springbatchtest.entity.DailyStockPrice;
import com.example.springbatchtest.repository.DailyStockPriceEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, DataJobConfig.class})
@ActiveProfiles("test")
class DailJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @MockBean(name = "dataItemReader")
    private FlatFileItemReader<DailyStockPriceDTO> dataItemReader;

    @Autowired
    private DailyStockPriceEntityRepository dailyStockPriceEntityRepository;

    @BeforeEach
    void setup(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @AfterEach
    void afterEach(){
        dailyStockPriceEntityRepository.deleteAll();
    }

    @Test
    void launchJobTest() throws Exception {
        DailyStockPriceDTO dailyStockPriceDTO = DailyStockPriceDTO.builder()
                .marketDate(LocalDate.of(2023, 03, 04))
                .openPrice(BigDecimal.valueOf(1L))
                .highPrice(BigDecimal.valueOf(1L))
                .lowPrice(BigDecimal.valueOf(1L))
                .closePrice(BigDecimal.valueOf(1L))
                .volume(1L)
                .openInt(1)
                .build();

        given(dataItemReader.read()).willReturn(dailyStockPriceDTO, null);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        System.out.println("DailJobConfigTest.launchJobTest dailyStockPrices size = " + dailyStockPrices.size());
    }

    @Test
    void launchJobTest2() throws Exception {
        DailyStockPriceDTO dailyStockPriceDTO = DailyStockPriceDTO.builder()
                .marketDate(LocalDate.of(2023, 03, 04))
                .openPrice(BigDecimal.valueOf(1L))
                .highPrice(BigDecimal.valueOf(1L))
                .lowPrice(BigDecimal.valueOf(1L))
                .closePrice(BigDecimal.valueOf(1L))
                .volume(1L)
                .openInt(1)
                .build();

        given(dataItemReader.read()).willReturn(dailyStockPriceDTO, null);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        System.out.println("DailJobConfigTest.launchJobTest2 dailyStockPrices size = " + dailyStockPrices.size());

    }

}
