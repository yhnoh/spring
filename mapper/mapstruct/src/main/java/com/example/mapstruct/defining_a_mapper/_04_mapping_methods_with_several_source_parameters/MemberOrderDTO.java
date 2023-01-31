package com.example.mapstruct.defining_a_mapper._04_mapping_methods_with_several_source_parameters;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberOrderDTO {
    private String username;
    private String orderName;
}
