package com.example.associatedrelationship;


import com.example.associatedrelationship.mapping.entity.Goods;
import com.example.associatedrelationship.mapping.repository.GoodsJpaRepository;
import com.example.associatedrelationship.mapping.entity.Order;
import com.example.associatedrelationship.mapping.repository.GoodsOrderJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
@DataJpaTest
public class RelationshipMappingTest {

    @Autowired
    GoodsJpaRepository goodsJpaRepository;

    @Autowired
    GoodsOrderJpaRepository goodsOrderJpaRepository;

    @Test
    public void orderTest(){
        Goods goods = Goods.createGoods("상품1", 100, 100, 10);
        Goods saveGoods = goodsJpaRepository.save(goods);

        Order goodsOrder = Order.createGoodsOrder(goods, 10);
        Order saveGoodsOrder = goodsOrderJpaRepository.save(goodsOrder);

        Assertions.assertNotEquals(0, saveGoods.getGoodsOrders().size());
    }


    @Test
}
