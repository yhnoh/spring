package com.example.associatedrelationship.mapping;

import com.example.associatedrelationship.mapping.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class OrderDTO {
    private String username;
    private String address;
    private List<GoodsOrderDTO> goodsOrderDTOs = new ArrayList<>();

    @Builder
    @Getter
    public static class GoodsOrderDTO{
        private Goods goods;
        private int orderQuantity;
    }
}
