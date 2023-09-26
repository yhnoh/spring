package com.example.springcloudstreamgoods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoodsEventListener {

    private final StreamBridge streamBridge;
    private static final String EVENT_NAME = "goodsChangeEvent-out-0";



    @EventListener
    public void goodsChangeEventListener(long goodsId){
        streamBridge.send(EVENT_NAME, goodsId);
    }

}
