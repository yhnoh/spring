package com.example.springbatchexample.customer_update;

import com.example.springbatchexample.customer_update.dto.CustomerAddressUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerContactUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerNameUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CustomerUpdateStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step customerUpdateStep() throws Exception {
        return stepBuilderFactory.get("customerUpdateStep")
                .<CustomerUpdate, CustomerUpdate>chunk(100)
                .reader(this.customerUpdateItemReader(null))
                .processor(this.customerUpdateItemProcessor(null))
                .writer(this.customerUpdateItemWriter())
                .build();
    }


    /**
     * customer_update.csv 파일에는 3가지 레코드 유형이 있다.
     * 가장 앞에 잇는 숫자를 통해서 유형을 구분한다.
     */
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerUpdate> customerUpdateItemReader(@Value("#{jobParameters['customerUpdateFile']}") Resource resource) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
                .name("customerUpdateFile")
                .resource(resource)
                .lineTokenizer(this.customerUpdateLineTokenizer())
                .fieldSetMapper(this.customerUpdateFieldSetMapper())
                .build();
    }

    /**
     * PatternMatchingCompositeLineTokenizer는 해당 패턴을 기반으로 LineTokenizer에게 파싱 처리를 위임한다.
     * 복수의 Tokenizer를 구성하여 각 레코드 유형을 식별하는 접두사 패턴에 각 인스턴스를 매핑한다.
     */
    @Bean
    public LineTokenizer customerUpdateLineTokenizer() throws Exception {
        DelimitedLineTokenizer recodeType1 = new DelimitedLineTokenizer();
        recodeType1.setNames("recordId", "customerId", "firstName", "middleName", "lastName");
        recodeType1.afterPropertiesSet();

        DelimitedLineTokenizer recodeType2 = new DelimitedLineTokenizer();
        recodeType2.setNames("recordId", "customerId", "address1", "address2", "city", "state", "postalCode");
        recodeType2.afterPropertiesSet();

        DelimitedLineTokenizer recodeType3 = new DelimitedLineTokenizer();
        recodeType3.setNames("recordId", "customerId", "emailAddress", "homePhone", "cellPhone", "workPhone", "notificationPreferences");
        recodeType3.afterPropertiesSet();

        Map<String, LineTokenizer> tokenizers = new HashMap<>();
        tokenizers.put("1*", recodeType1);
        tokenizers.put("2*", recodeType2);
        tokenizers.put("3*", recodeType3);

        PatternMatchingCompositeLineTokenizer lineTokenizer = new PatternMatchingCompositeLineTokenizer();
        lineTokenizer.setTokenizers(tokenizers);

        return lineTokenizer;
    }

    /**
     * 어떤 객체를 반환할지 결정하려면 레코드 유형에 맞는 올바른 도메인 객체를 생성하고 반환하는 FieldSetMapper가 필요하다.
     */
    @Bean
    public FieldSetMapper<CustomerUpdate> customerUpdateFieldSetMapper(){
        return fieldSet -> {
            switch (fieldSet.readInt("recordId")){
                case 1: return new CustomerNameUpdate(fieldSet.readLong("customerId"),
                        fieldSet.readString("firstName"),
                        fieldSet.readString("middleName"),
                        fieldSet.readString("lastName")
                        );
                case 2: return new CustomerAddressUpdate(fieldSet.readLong("customerId"),
                        fieldSet.readString("address1"),
                        fieldSet.readString("address2"),
                        fieldSet.readString("city"),
                        fieldSet.readString("state"),
                        fieldSet.readString("postalCode"));
                case 3:
                    String readNotificationPreferences = fieldSet.readString("notificationPreferences");
                    Integer notificationPreferences = null;
                    if(StringUtils.hasText(readNotificationPreferences)){
                        notificationPreferences = Integer.parseInt(readNotificationPreferences);
                    }
                    return new CustomerContactUpdate(fieldSet.readLong("customerId"),
                        fieldSet.readString("emailAddress"),
                        fieldSet.readString("homePhone"),
                        fieldSet.readString("cellPhone"),
                        fieldSet.readString("workPhone"),
                            notificationPreferences
                        );
                default:
                    throw new IllegalArgumentException("Invalid record type was found : " + fieldSet.readInt("recordId"));
            }
        };
    }

    @Bean
    public ItemProcessor<CustomerUpdate, CustomerUpdate> customerUpdateItemProcessor(CustomerUpdateValidator validator) {
        ValidatingItemProcessor<CustomerUpdate> itemProcessor = new ValidatingItemProcessor<>(validator);
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER " +
                        "SET FIRST_NAME = COALESCE(:firstName, FIRST_NAME), " +
                        "MIDDLE_NAME = COALESCE(:middleName, MIDDLE_NAME), " +
                        "LAST_NAME = COALESCE(:lastName, LAST_NAME) " +
                        "WHERE CUSTOMER_ID = :customerId")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER SET " +
                        "ADDRESS1 = COALESCE(:address1, ADDRESS1), " +
                        "ADDRESS2 = COALESCE(:address2, ADDRESS2), " +
                        "CITY = COALESCE(:city, CITY), " +
                        "STATE = COALESCE(:state, STATE), " +
                        "POSTAL_CODE = COALESCE(:postalCode, POSTAL_CODE) " +
                        "WHERE CUSTOMER_ID = :customerId")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER SET " +
                        "EMAIL_ADDRESS = COALESCE(:emailAddress, EMAIL_ADDRESS), " +
                        "HOME_PHONE = COALESCE(:homePhone, HOME_PHONE), " +
                        "CELL_PHONE = COALESCE(:cellPhone, CELL_PHONE), " +
                        "WORK_PHONE = COALESCE(:workPhone, WORK_PHONE), " +
                        "NOTIFICATION_PREF = COALESCE(:notificationPreferences, NOTIFICATION_PREF) " +
                        "WHERE CUSTOMER_ID = :customerId")
                .dataSource(dataSource)
                .build();
    }



    @Bean
    public ItemWriter<CustomerUpdate> customerUpdateItemWriter() {
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(this.customerNameUpdateItemWriter(null),
                this.customerAddressUpdateItemWriter(null),
                this.customerContactUpdateItemWriter(null));

        ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(classifier);
        return compositeItemWriter;
    }

}
