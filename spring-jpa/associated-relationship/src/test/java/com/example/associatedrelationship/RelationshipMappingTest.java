package com.example.associatedrelationship;


import com.example.associatedrelationship.mapping.GoodsService;
import com.example.associatedrelationship.mapping.OrderDTO;
import com.example.associatedrelationship.mapping.entity.Goods;
import com.example.associatedrelationship.mapping.entity.OrderUserInfo;
import com.example.associatedrelationship.mapping.repository.GoodsJpaRepository;
import com.example.associatedrelationship.mapping.entity.Order;
import com.example.associatedrelationship.mapping.repository.GoodsOrderJpaRepository;
import com.example.associatedrelationship.mapping.repository.OrderJpaRepository;
import com.example.associatedrelationship.mapping.repository.OrderUserInfoJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
@DataJpaTest
@Import(GoodsService.class)
public class RelationshipMappingTest {

    @Autowired
    GoodsService goodsService;

    @Autowired
    GoodsJpaRepository goodsJpaRepository;
    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    OrderUserInfoJpaRepository orderUserInfoJpaRepository;
    @Test
    public void registerGoodsTest(){
        Goods goods = goodsService.registerGoods("상품3", 100, 200, 10);

        Assertions.assertEquals("상품3", goods.getName());
    }


    @Test
    public void orderTest(){
        List<String> names = Arrays.asList("상품1", "상품2", "상품3");
        List<Goods> goodsList = goodsJpaRepository.findByNameIn(names);
        List<OrderDTO.GoodsOrderDTO> goodsOrderDTOS = new ArrayList<>();
        for (Goods goods : goodsList) {
            goodsOrderDTOS.add(OrderDTO.GoodsOrderDTO.builder()
                    .goods(goods)
                    .orderQuantity(5)
                    .build());
        }


        OrderDTO orderDTO = OrderDTO.builder()
                .username("사용자1")
                .address("주소1")
                .goodsOrderDTOs(goodsOrderDTOS)
                .build();
        Order order = goodsService.order(orderDTO);


    }

    @Test
    public void oneToOneNPlusOneTest(){
        Order order = orderJpaRepository.findById(1l).get();
    }
}
