package com.example.springbatchexample.customer_update;

import com.example.springbatchexample.BankJobConfig;
import com.example.springbatchexample.account_statement.AccountStatementItemProcessor;
import com.example.springbatchexample.account_statement.AccountStatementStepConfig;
import com.example.springbatchexample.customer_update.dto.CustomerAddressUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerContactUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerNameUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import com.example.springbatchexample.transaction.AccountUpdateStepConfig;
import com.example.springbatchexample.transaction.TransactionStepConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Spring Batch에서 스텝 스코프나 잡 스코프를 사용하는 컴포넌트의 통합 테스트를 작성할 때 어떻게 해야 할까? <br/>
 * TestExecutionListener는 테스트 메서드 실행 전후에 수행돼야 하는 일을 정의하는 스프링 API 이다. <br/>
 * @SpringBatchTest 어노테이션 안에 StepScopeTestExecutionListener.class, JobScopeTestExecutionListener.class를 임포트 해준다. <br/>
 *
 * @SpringBatchTest ApplicationContext에 자동으로 테스트할 수 있는 많은 유틸리티를 제공한다.
 * <li>잡이나 스텝을 실행하는 JobLauncherTestUtils 인스턴스<li/>
 * <li>JobRepository에서 JobExecutions를 생성하는데 사용하는 JobRepositoryTestUtils<li/>
 * <li>스텝 스코프와 잡 스코프 빈을 테스트 할 수 있는 StepScopeTestExecutionListener.class, JobScopeTestExecutionListener.class<li/>
 */
@ExtendWith(SpringExtension.class)
@JdbcTest
@SpringBatchTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ContextConfiguration(classes = {CustomerUpdateStepConfig.class,
        CustomerUpdateValidator.class,
        BankJobConfig.class,
        AccountUpdateStepConfig.class,
        BatchAutoConfiguration.class,
        TransactionStepConfig.class,
        AccountStatementItemProcessor.class,
        AccountStatementStepConfig.class})
public class CustomerUpdateStepConfigTest {


    @Autowired
    private FlatFileItemReader<CustomerUpdate> customerUpdateItemReader;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @BeforeEach
    void setUp() {
    }

    /**
     * StepScopeTestExecutionListener를 사용해 스텝 스코프 의존성을 처리하려면, 필요한 항목으로 채워진 StepExcution을 제공해야한다.
     * 테스트 대상 아이템 리더가 읽을 파일을 가리키는 잡 파라미터를 전달해야한다.
     *
     * MetaDataInstanceFactory는 StepExecution과 JobExecution 인스턴스를 생성하는 유틸리티 클래스다.
     */
    public StepExecution getStepExecution(){
        JobParameters jobParameters = new JobParametersBuilder().addString("", "classpath:data/customer_update.csv")
                .toJobParameters();
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    void test() throws Exception {
        customerUpdateItemReader.open(new ExecutionContext());

        assertTrue(customerUpdateItemReader.read() instanceof CustomerAddressUpdate);
        assertTrue(customerUpdateItemReader.read() instanceof CustomerNameUpdate);
        assertTrue(customerUpdateItemReader.read() instanceof CustomerContactUpdate);

    }

    @Test
    public void customerUpdateStepTest(){

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerUpdateFile", "classpath:customerFile.csv")
                .toJobParameters();

        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("customerUpdateStep", jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}