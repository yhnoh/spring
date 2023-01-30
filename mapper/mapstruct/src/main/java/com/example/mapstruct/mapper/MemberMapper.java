package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.dto.OrderDTO;
import com.example.mapstruct.entity.Member;
import com.example.mapstruct.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * 스프링 컨테이너에서 Bean객체로 관리하여 주입받을 수 있는 방법
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    /**
     * Mappers Factory를 이용하여 직접 접근
     */
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    Member toMember(MemberDTO memberDTO);

    /**
     * 양방향 연관관계의 엔티티를 매핑시 순환 참조가 발생할 수 있다. 때문에 특정 매핑을 무시해야한다.
     * <p>
     * Collection 내의 값에 대한 필드를 바로 접근하는 방법을 찾지 못하여, 아래와 같이 해당 방법을 통해서 접근한다.
     * 왠만하면 DTO 간에 양방향 연관관계를 맺지 않는것을  추천한다.
     */
    @Mapping(target = "orders", qualifiedByName = "toOrderDTO")
    MemberDTO toMemberDTO(Member member);

    @Mapping(target = "member", ignore = true)
    @Named("toOrderDTO")
    OrderDTO toOrderDTO(Order order);
}
