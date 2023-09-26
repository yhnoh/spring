package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CloudStreamConfig {

    private final CacheManager cacheManager;
    @Bean
    public Consumer<Long> goodsChangeEvent(){
        return goodsId -> {
            Collection<String> cacheNames = cacheManager.getCacheNames();
            Set<String> goods = cacheNames.stream().filter(cacheName -> cacheName.equals(CacheConfig.GOODS_CACHE_NAME)).collect(Collectors.toSet());
            for (String good : goods) {
                Cache cache = cacheManager.getCache(good);
                cache.evict(goodsId);
            }

            log.info("상품아이디 {}가 변경되었습니다.", goodsId);
        };
    }

}
