package com.example.springbatchexample.customer_update;

import com.example.springbatchexample.customer_update.dto.CustomerAddressUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CustomerUpdateValidatorTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private CustomerUpdateValidator customerUpdateValidator;

    @BeforeEach
    void setUp() {
        //@Mock 대상을 목 객체로 초기화
        MockitoAnnotations.openMocks(this);
        this.customerUpdateValidator = new CustomerUpdateValidator(this.namedParameterJdbcTemplate);
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
            ArgumentCaptor<Map<String, Object>> parameterMap = ArgumentCaptor.forClass(Map.class);
            when(namedParameterJdbcTemplate.queryForObject(any(), parameterMap.capture(), eq(Long.class)))
                    .thenReturn(2L);

            customerUpdateValidator.validate(customerUpdate);

            //then
            Assertions.assertThat(5L).isEqualTo(parameterMap.getValue().get("id"));

        }

        @Test
        void invalidTest(){

            //given
            CustomerUpdate customerUpdate = new CustomerUpdate(5L);

            //when
            // ArgumentCaptor : 추후 분석을 하기 위해 파라미터 캡쳐
            ArgumentCaptor<Map<String, Object>> parameterMap = ArgumentCaptor.forClass(Map.class);
            when(namedParameterJdbcTemplate.queryForObject(any(), parameterMap.capture(), eq(Long.class)))
                    .thenReturn(0L);


            ValidationException exception = assertThrows(ValidationException.class, () -> customerUpdateValidator.validate(customerUpdate));
            //then

            assertEquals("Customer id 5 was not able to be found", exception.getMessage());

        }

    }
}