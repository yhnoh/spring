package com.example.springcloudstreamgoods;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringCloudStreamGoodsApplicationTests {
	@Autowired
	private GoodsEventListener goodsEventListener;
	@Test
	void contextLoads() {
//		streamBridge.send("")
		goodsEventListener.goodsChangeEventListener(1L);
//		boolean send = streamBridge.send("goodsCacheEvict-out-0", 1L);
	}

}
