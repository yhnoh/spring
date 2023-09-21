package com.example.springcloudstreamorder;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash("goods")
public class GoodsRedisEntity {

    @Id
    private long id;
    private String name;
    private int quantity;
    private int amount;

}
