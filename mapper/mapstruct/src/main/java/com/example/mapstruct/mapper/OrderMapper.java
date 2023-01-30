package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.OrderDTO;
import com.example.mapstruct.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderDTO toOrderDTO(Order order);
}
