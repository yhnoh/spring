package com.example.mapstruct.defining_a_mapper._03_adding_custom_methods_to_mappers;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import com.example.mapstruct.defining_a_mapper.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMethodMapper {


    MemberDTO toAutoMemberDTO(Member member);

    default List<OrderDTO> toAutoOrderDTOs(List<Order> orders) {
        return orders.stream().map(order -> OrderDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .quantity(order.getQuantity())
                .createdDatetime(order.getCreatedDatetime())
                .build()).collect(Collectors.toList());
    }

    @Mapping(target = "orders", qualifiedByName = "ToOrderDTO")
    MemberDTO toQualifiedMemberDTO(Member member);

    @Mapping(target = "id", ignore = true)
    @Named("ToOrderDTO")
    OrderDTO toQualifiedOrderDTO(Order order);
}