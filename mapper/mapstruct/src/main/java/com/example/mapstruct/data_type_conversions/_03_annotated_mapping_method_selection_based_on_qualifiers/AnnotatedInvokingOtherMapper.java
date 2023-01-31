package com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers;

import com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation.LocalDateTimeToString;
import com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation.StringToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * uses 필드를 사용하여 필요한 Data Mapper 호출 가능
 * 모든 메서드에 전역적으로 반영됨으로 사용하기 까다로울 수 있다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AnnotatedInvokedMapper.class)
public interface AnnotatedInvokingOtherMapper {


    @Mapping(target = "createdDateTime", qualifiedBy = LocalDateTimeToString.class)
    @Mapping(target = "username", qualifiedBy = StringToString.class)
    MemberDTO toMemberDTO(Member member);

}
