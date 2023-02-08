package com.example.springtestcontainer.features;

import com.example.springtestcontainer.config.RedisConfig;
import com.github.dockerjava.api.model.Bind;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@Slf4j
@SpringBootTest(classes = RedisConfig.class)
@ActiveProfiles("test")
public class CreatingContainerTest {

    @Container
    private static final GenericContainer REDIS_CONTAINER = new GenericContainer("redis:7.0.8-alpine")
            .withExposedPorts(6379);

    @Autowired
    private RedisTemplate redisTemplate;
    private final String KEY="keyword";

    @DynamicPropertySource
    public static void setDynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));

    }

    @Test
    public void containerTest(){
        assertThat(REDIS_CONTAINER.isCreated()).isEqualTo(true);
        assertThat(REDIS_CONTAINER.isRunning()).isEqualTo(true);
    }

    @Test
    public void redisTest(){
        //given
        String keyword="한남동 맛집";
        String keyword2="서촌 맛집";

        //when
        redisTemplate.opsForZSet().add(KEY,keyword,1);
        redisTemplate.opsForZSet().incrementScore(KEY,keyword,1);
        redisTemplate.opsForZSet().incrementScore(KEY,keyword,1);

        redisTemplate.opsForZSet().add(KEY,keyword2,1);

        //then
        System.out.println(redisTemplate.opsForZSet().popMax(KEY));
        System.out.println(redisTemplate.opsForZSet().popMin(KEY));
    }
}
