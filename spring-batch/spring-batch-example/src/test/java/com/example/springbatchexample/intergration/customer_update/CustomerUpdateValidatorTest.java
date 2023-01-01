package com.example.springbatchexample.intergration.customer_update;

import com.example.springbatchexample.customer_update.CustomerUpdateValidator;
import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * hsqldb 인메모리 데이터베이스를 활용하여 테스트
 * resources 폴더의 data.sql 및 schema.sql를 활용하여 테스트를 진행한다.
 */
@ExtendWith(SpringExtension.class)
@JdbcTest
class CustomerUpdateValidatorTest {

    @Autowired
    private DataSource dataSource;

    private CustomerUpdateValidator customerUpdateValidator;

    @BeforeEach
    void setUp() {
        //@Mock 대상을 목 객체로 초기화
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.customerUpdateValidator = new CustomerUpdateValidator(namedParameterJdbcTemplate);
    }

    /**
     * CustomerUpdateValidator.validate(CustomerUpdate)에는 두가지 테스트 경우가 있다.
     * 한가지 경우에는 고객정보가 존재하는 경우, 다른 경우는 고객 정보가 없는 경우이다.
     */

    @Nested
    public class Validate{

        @Test
        void validTest(){

            //given
            CustomerUpdate customerUpdate = new CustomerUpdate(5L);

            //when
            // ArgumentCaptor : 추후 분석을 하기 위해 파라미터 캡쳐
            customerUpdateValidator.validate(customerUpdate);

        }

        @Test
        void invalidTest(){

            //given
            CustomerUpdate customerUpdate = new CustomerUpdate(-5L);

            //when
            ValidationException exception = assertThrows(ValidationException.class, () -> customerUpdateValidator.validate(customerUpdate));

            //then
            assertEquals("Customer id -5 was not able to be found", exception.getMessage());

        }
    }
}