package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final GoodsOpenFeign goodsOpenFeign;
    public void order(OrderRequest orderRequest){
        long goodsId = orderRequest.getGoodsId();
        Goods goods = goodsOpenFeign.getGoodsById(goodsId);
        goods.changeQuantity(orderRequest.getQuantity());

        OrderEntity.builder().goodsId(goodsId).quantity(orderRequest.getQuantity()).amount(orderRequest.getAmount())
                        .name(or)
        orderJpaRepository.save()

    }
}
