package com.example.mapstruct.defining_a_mapper._03_adding_custom_methods_to_mappers;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberOrdersDTO {

    private String username;
    private List<OrderDTO> orders;
}
