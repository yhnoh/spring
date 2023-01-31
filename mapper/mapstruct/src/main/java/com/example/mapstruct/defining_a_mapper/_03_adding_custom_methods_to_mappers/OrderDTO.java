package com.example.mapstruct.defining_a_mapper._03_adding_custom_methods_to_mappers;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class OrderDTO {

    private Long id;
    private String orderName;
    private LocalDateTime createdDatetime;
    private Integer quantity;
}
