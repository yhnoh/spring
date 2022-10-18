package com.example.associatedrelationship.mapping;

import com.example.associatedrelationship.mapping.entity.Goods;
import com.example.associatedrelationship.mapping.entity.GoodsOrder;
import com.example.associatedrelationship.mapping.entity.Order;
import com.example.associatedrelationship.mapping.entity.OrderUserInfo;
import com.example.associatedrelationship.mapping.repository.GoodsJpaRepository;
import com.example.associatedrelationship.mapping.repository.GoodsOrderJpaRepository;
import com.example.associatedrelationship.mapping.repository.OrderJpaRepository;
import com.example.associatedrelationship.mapping.repository.OrderUserInfoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsService {
    private final GoodsJpaRepository goodsJpaRepository;
    private final GoodsOrderJpaRepository goodsOrderJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderUserInfoJpaRepository orderUserInfoJpaRepository;

    public Goods registerGoods(String name, int totalQuantity, double price, double discountPirce){
        Goods goods = Goods.createGoods(name, totalQuantity, price, discountPirce);
        return goodsJpaRepository.save(goods);
    }

    public Order order(OrderDTO orderDTO){
        List<GoodsOrder> goodsOrders = new ArrayList<>();
        List<OrderDTO.GoodsOrderDTO> goodsOrderDTOs = orderDTO.getGoodsOrderDTOs();

        for (OrderDTO.GoodsOrderDTO goodsOrderDTO : goodsOrderDTOs) {
            GoodsOrder goodsOrder = GoodsOrder.createGoodsOrder(goodsOrderDTO.getGoods(), goodsOrderDTO.getOrderQuantity());
            goodsOrders.add(goodsOrder);
        }

        Order order = Order.createOrder(goodsOrders);
        orderJpaRepository.save(order);
        goodsOrderJpaRepository.saveAll(goodsOrders);
        OrderUserInfo orderUserInfo = OrderUserInfo.createOrderUserInfo(orderDTO.getUsername(), orderDTO.getAddress(), order);
        orderUserInfoJpaRepository.save(orderUserInfo);

        return order;
    }

}
