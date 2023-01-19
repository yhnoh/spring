package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

//    Member toEntity(MemberDTO memberDTO);

    MemberDTO toDTO(Member member);
}
