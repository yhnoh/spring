package com.example.mapstruct.data_type_conversions._01_invoking_other_mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * uses 필드를 사용하여 필요한 Data Mapper 호출 가능
 * 동일한 맵퍼 클래스에 정의된 메서드 외에도 다른 클래스에 정의된 매핑 메서드를 호출 가능
 * MapStruct에서 생성할 수 없는 사용자 지정 매핑 로직을 제공하려는 경우에 유용
 * 모든 매핑 클래스에 전역적으로 반영됨으로 핸들링이 까다로움.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = InvokedMapper.class)
public interface InvokingOtherMapper {

    MemberDTO toMemberDTO(Member member);

}
