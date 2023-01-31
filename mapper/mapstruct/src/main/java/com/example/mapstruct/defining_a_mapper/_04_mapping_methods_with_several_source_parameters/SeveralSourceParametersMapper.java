package com.example.mapstruct.defining_a_mapper._04_mapping_methods_with_several_source_parameters;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import com.example.mapstruct.defining_a_mapper.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeveralSourceParametersMapper {

    @Mapping(target = "username", source = "member.username")
    @Mapping(target = "orderName", source = "order.orderName")
    MemberOrderDTO toMemberOrderDTO(Member member, Order order);
}
