package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final GoodsOpenFeign goodsOpenFeign;
    private final OrderGoodsMapper orderGoodsMapper;

    public List<OrderGoods> orderGoodsList(){
        List<OrderEntity> orderEntities = orderJpaRepository.findAll();
        List<Goods> goodsList = goodsOpenFeign.getGoodsList();

        return orderEntities.stream().map(orderEntity -> {
            Goods findGoods = goodsList.stream().filter(goods -> goods.getId() == orderEntity.getGoodsId()).findAny().orElse(null);
            return orderGoodsMapper.toOrder(orderEntity, findGoods);
        }).collect(Collectors.toList());
    }

    public OrderGoods getOrderGoods(long id) {
        return orderJpaRepository.findById(id).map(orderEntity1 -> {
            Goods goods = goodsOpenFeign.getGoodsById(orderEntity1.getGoodsId());
            return orderGoodsMapper.toOrder(orderEntity1, goods);
        }).orElse(null);
    }

    public OrderGoods order(OrderRequest orderRequest){

        long goodsId = orderRequest.getGoodsId();
        Goods goods = goodsOpenFeign.getGoodsById(goodsId);
        if(goods == null){
            throw new IllegalArgumentException("주문할 상품 정보가 없습니다.");
        }

        goods.changeQuantity(orderRequest.getQuantity());

        OrderEntity orderEntity = OrderEntity.builder()
                .goodsId(goodsId)
                .name(goods.getName())
                .quantity(orderRequest.getQuantity())
                .amount(goods.getAmount())
                .totalAmount(orderRequest.getQuantity() * goods.getAmount())
                .build();

        orderJpaRepository.save(orderEntity);
        goodsOpenFeign.updateGoods(goodsId, goods);
        return orderGoodsMapper.toOrder(orderEntity, goods);
    }

}
