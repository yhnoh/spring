package com.example.mapstruct.data_type_conversions._02_named_mapping_method_selection_based_on_qualifiers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * 특정 메서드에 대해서만 반영이 가능
 * 같은 이름을 가진 Data Mapper 메서드가 두개 이상일 경우 에러가 발생
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = NamedInvokedMapper.class)
public interface NamedInvokingOtherMapper {


    @Mapping(target = "createdDateTime", qualifiedByName = "LocalDateTimeToString")
    @Mapping(target = "modifiedDateTime", qualifiedByName = {"NamedInvokedMapper", "StringToLocalDateTime"})
    MemberDTO toMemberDTO(Member member);

}
