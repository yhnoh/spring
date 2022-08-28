package com.example.querydsl.repository;

import com.example.querydsl.entity.dto.MemberSearchCondition;
import com.example.querydsl.entity.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition);

    List<MemberTeamDto> searchByParameters(MemberSearchCondition condition);

    Page<MemberTeamDto> searchPaging(MemberSearchCondition condition, Pageable pageable);
}
