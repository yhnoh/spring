package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.dto.OrderDTO;
import com.example.mapstruct.entity.Member;
import com.example.mapstruct.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(target = "member", qualifiedByName = "toMemberDTO")
    OrderDTO toOrderDTO(Order order);

    @Mapping(target = "orders", ignore = true)
    @Named("toMemberDTO")
    MemberDTO toMemberDTO(Member member);
}
