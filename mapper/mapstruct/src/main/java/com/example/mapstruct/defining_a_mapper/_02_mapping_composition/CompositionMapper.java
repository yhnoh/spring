package com.example.mapstruct.defining_a_mapper._02_mapping_composition;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompositionMapper {

    @ToEntity
    @Mapping(target = "memberName", source = "username")
    MemberDTO toMemberDTO(Member member);
}
